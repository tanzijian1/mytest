package nc.impl.tg.outside.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pnt.vo.FileManageVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.accruedexpense.AccruedVerifyVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.erm.matterapp.MtAppDetailVO;
import nc.vo.erm.matterapp.MtAppPayplanVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.ResultVO;

public class PushBpmDataUtils {

	private IUAPQueryBS bs;
	static PushBpmDataUtils utils = null;

	public static PushBpmDataUtils getUtils() {
		if (utils == null) {
			utils = new PushBpmDataUtils();
		}
		return utils;
	}

	/**
	 * ��װ����������
	 * 
	 * @param billtype
	 * @param jkbxvo
	 * @return
	 * @throws BusinessException
	 */
	public ResultVO pushLLBillToBpm(String billtype, JKBXVO jkbxvo)
			throws BusinessException {
		String lock = jkbxvo.getParentVO().getPrimaryKey() + "&ERM";
		// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("�����ύ��BPM,��ȴ�BPM��Ϣ����!");
		}
		try {
			JKBXHeaderVO parent = jkbxvo.getParentVO();
			String bpmid = parent.getZyx30();
			String postcode = getPostcode(parent.getZyx1());
			String deptid = null;
			if (postcode != null) {
				deptid = getDeptID(postcode);
			}
			Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
			purchase.put("Title", parent.getZyx22());// ����
			purchase.put("Applicant", getOperator_name(parent.getOperator()));// ������
			purchase.put("ApplicantCode",
					getOperator_code(parent.getOperator()));// �������˺�
			purchase.put("ApplicationDate", parent.getDjrq().toString());// ��������
			purchase.put("ApplicationCompany",
					getDwbm_v_name(parent.getDwbm_v()));// ���빫˾
			purchase.put("ApplicationCompanyCode",
					getDwbm_v_code(parent.getDwbm_v()));// ���빫˾����
			purchase.put("ApplicationDepartment",
					getDeptid_v_name(parent.getDeptid_v()));// ���벿��
			purchase.put("ApplicationDepartmentCode",
					getDeptid_v_code(parent.getDeptid_v()));// ���벿�ű���
			purchase.put("djbh", parent.getDjbh());// NC���ݱ��
			purchase.put("pk_org_v_name",
					getPk_org_v_name(parent.getPk_org_v()));// ���˹�˾
			purchase.put("djrq", parent.getDjrq().toString());// ��������
			purchase.put("total", parent.getTotal() == null ? 0 : parent
					.getTotal().getDouble());// ������
			purchase.put("zyx2", parent.getZyx2());// ��ͬ����
			purchase.put("zyx9", getContract_name(parent.getZyx9()));// ��ͬ����
			purchase.put("paytarget", parent.getPaytarget() == 0 ? "Ա��"
					: parent.getPaytarget() == 1 ? "��Ӧ��" : "�ͻ�");// �տ����
			purchase.put("receiver_name",
					getReceiver_name(parent.getReceiver()));// �տ���
			purchase.put("skyhzh_accname", getSkyhzh_accnum(parent.getSkyhzh()));// �տ��˻�
			purchase.put("zyx50", parent.getZyx50());// ������
			purchase.put("zyx16", parent.getZyx16());// Ӱ�����
			purchase.put("def67", parent.getDef67());// �������

			String jsfsName = getJsfs("name", parent.getJsfs());
			purchase.put("jsfs_name", jsfsName);// ���㷽ʽ
			purchase.put("fydwbm_name", getFydwbm_v_name(parent.getFydwbm_v()));// Ԥ�������λ

			// ������Ϣ����
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
			purchaseDetaillist = getLLPurchaseDetaillist(jkbxvo);
			// Ԥ����Ϣ����
			List<Map<String, Object>> accruedVerifyList = new ArrayList<>();// Ԥ����Ϣ����list
			if (jkbxvo.getAccruedVerifyVO().length != 0) {
				accruedVerifyList = getLLAccruedVerifyList(jkbxvo);
			}

			Map<String, Object> formData = new HashMap<String, Object>();// ������
			if (billtype.equals("264X-Cxx-LL01")) {
				formData.put("I_ReimbursementEmergencyPurchaseLL", purchase);
				formData.put("C_ReimbursementDetailLL", purchaseDetaillist);
			} else if (billtype.equals("264X-Cxx-LL02")) {
				formData.put("I_TravelExpenseLL", purchase);
				formData.put("C_TraveDetailLL", purchaseDetaillist);
				formData.put("C_ExpenseDetailLL", accruedVerifyList);
			} else if (billtype.equals("264X-Cxx-LL03")) {
				formData.put("I_NotTravelExpenseLL", purchase);
				formData.put("C_NotTraveDetail", purchaseDetaillist);
				formData.put("C_WriteOffWithdraw", accruedVerifyList);
			}

			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			String ProcessName = getIUAPQueryBS("SELECT exsysdes FROM xx_bdcontra_b where bdcode = '"
					+ billtype + "'");// ����������ϸ����
			if (billtype.equals("264X-Cxx-LL01")
					|| billtype.equals("264X-Cxx-LL02")
					|| billtype.equals("264X-Cxx-LL03")) {
				postdata.put("ProcessName", ProcessName);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid);
				postdata.put("AttachmentInfo", getFiles(parent.getPk_jkbx()));
				postdata.put("ExistTaskID", bpmid);
				postdata.put("usercode", getUser_code(parent.getCreator()));
			}

			ResultVO vo = TGCallUtils.getUtils().onDesCallService(
					parent.getPrimaryKey(), "BPM", "PostTask", postdata);
			return vo;
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(e.getMessage(), e);
		} finally {
			// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
			PKLock.getInstance().releaseLock(lock, null, null);
		}
	}

	/**
	 * ��ȡ���ﵥ����ϸ����
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(JKBXVO jkbxvo) {
		String djlxbm = jkbxvo.getParentVO().getDjlxbm();
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		for (BXBusItemVO vos : jkbxvo.getChildrenVO()) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������

			purchaseDetail.put("defitem48", vos.getDefitem48());// Ԥ��ȫ��
			purchaseDetail.put("jobid_project_name",
					getPproject_name(vos.getJobid()));// ��Ŀ
			purchaseDetail.put("jkbxr_name", getJkbxr_name(vos.getJkbxr()));// �ÿ���
			purchaseDetail
					.put("defitem15", getInvoice_name(vos.getDefitem15()));// ��Ʊ����
			purchaseDetail.put("defitem18", vos.getDefitem18());// ��Ʊ��
			purchaseDetail.put("defitem23", vos.getDefitem23());// ��Ʊ���
			purchaseDetail.put("ybje", vos.getYbje() == null ? 0 : vos
					.getYbje().getDouble());// ԭ�ҽ��
			purchaseDetail.put("defitem16", vos.getDefitem16());// ˰��
			purchaseDetail.put("defitem13", vos.getDefitem13());// ����˰���
			purchaseDetail.put("defitem14", vos.getDefitem14());// ˰��
			purchaseDetail.put("defitem30", vos.getDefitem30());// ��ע
			purchaseDetail.put("amount", vos.getAmount() == null ? 0 : vos
					.getAmount().getDouble());// �����
			if ("264X-Cxx-LL02".equals(djlxbm)
					|| "264X-Cxx-LL03".equals(djlxbm)) {
				if ("264X-Cxx-LL02".equals(djlxbm)) {
					purchaseDetail.put("defitem1", vos.getDefitem1());// ��������
					purchaseDetail.put("defitem2", vos.getDefitem2());// ��������
					purchaseDetail.put("defitem3", vos.getDefitem3());// �����ص�
					purchaseDetail.put("defitem4", vos.getDefitem4());// ����ص�
				}
				purchaseDetail.put("defitem20",
						getCosttype_name(vos.getDefitem20()));// ��������
			}

			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	/**
	 * ��ȡ����Ԥ���������
	 * 
	 * @param jkbxvo
	 * @return
	 * @author KongYL
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getLLAccruedVerifyList(JKBXVO jkbxvo)
			throws BusinessException {
		List<Map<String, Object>> accruedVerifyList = new ArrayList<>();
		for (AccruedVerifyVO accVerVo : jkbxvo.getAccruedVerifyVO()) {
			Map<String, Object> accruedDetail = new HashMap<String, Object>();
			// -----------------
			// ��������Ԥ����ϸ er_accrued_verify
			accruedDetail.put("accrued_billno", accVerVo.getAccrued_billno());// Ԥ�ᵥ�ݱ��
			accruedDetail.put("verify_amount",
					accVerVo.getVerify_amount() == null ? 0 : accVerVo
							.getVerify_amount().getDouble());// �������

			accruedDetail.put("verify_man_user_name",
					getSmUserName(accVerVo.getVerify_man()));// ������
			// accruedDetail.put("defitem12", accVerVo.getVerify_amount());//
			// accruedDetail.put("defitem30", accVerVo.get);//

			accruedVerifyList.add(accruedDetail);
		}
		return accruedVerifyList;
	}

	/**
	 * ��װ�������뵥�������͵�BPM
	 * 
	 * @param billtype
	 *            ��������
	 * @param aggvo
	 *            AggMatterAppVO
	 * @throws BusinessException
	 */
	public ResultVO pushMattBillToBpm(String billtype, AggMatterAppVO aggvo)
			throws BusinessException {
		String lock = aggvo.getParentVO().getPrimaryKey() + "&BPM";
		// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("�����ύ��BPM,��ȴ�BPM��Ϣ����!");
		}
		try {
			MatterAppVO parent = aggvo.getParentVO();
			String creator = getUser_code(parent.getCreator());
			String bpmid = parent.getDefitem1();

			Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����

			Map<String, String> map = getApplyer_info(parent.getBillmaker());
			purchase.put("Title", parent.getDefitem45());// ����
			purchase.put("Applicant", map.get("user_name"));// ������
			purchase.put("ApplicantCode", map.get("user_code"));// �������˺�
			purchase.put("ApplicationDate", parent.getBilldate().toString());// ��������

			map = getOrg_info(parent.getApply_org());
			purchase.put("ApplicationCompany", map.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", map.get("code"));// ���빫˾����

			map = getDept_info(parent.getApply_dept());
			purchase.put("ApplicationDepartment", map.get("name"));// ���벿��
			purchase.put("ApplicationDepartmentCode", map.get("code"));// ���벿�ű���
			purchase.put("djbh", parent.getBillno());// NC���ݱ��
			purchase.put("pk_org_v_name",
					getPk_org_v_name(parent.getPk_org_v()));// ���˹�˾����
			purchase.put("pk_org_v_code",
					getPk_org_v_code(parent.getPk_org_v()));// ���˹�˾����
			purchase.put("djrq", parent.getBilldate().toString());// ��������
			String def15 = parent.getDefitem15();
			String sql = " select code  " + "  from bd_defdoc  "
					+ " where pk_defdoc = '" + def15 + "'";
			String paytarget = getIUAPQueryBS(sql);
			if (!StringUtil.isEmpty(paytarget)) {
				purchase.put(
						"defitem15",
						Integer.valueOf(paytarget) == 0 ? "Ա��" : Integer
								.valueOf(paytarget) == 1 ? "��Ӧ��" : "�ͻ�");// �տ����
			}

			map = getApplyer_info(parent.getDefitem16());
			purchase.put("defitem16", map.get("user_name"));// �տ���
			purchase.put("defitem18", parent.getDefitem18());// ���������˻�
			purchase.put("pk_supplier",
					getPk_supplier_name(parent.getPk_supplier()));// ��Ӧ��
			purchase.put("defitem19", parent.getDefitem19());// �տ����п���������
			purchase.put("defitem20", parent.getDefitem20());// �տ����п����б���
			purchase.put("defitem21",
					getCustaccount_accnum(parent.getDefitem21()));// �տ������˺�
			purchase.put("defitem14", parent.getDefitem14());// �Ƿ�Ԥ��
			purchase.put("iscostshare", parent.getDefitem50());// �Ƿ��̯��ͬ
			purchase.put("defitem7", parent.getDefitem7());// ��ͬ���
			if (("261X-Cxx-LL01").equals(billtype)) {
				purchase.put("defitem8", parent.getDefitem8());// ��ͬ����
			} else if (("261X-Cxx-LL02").equals(billtype)) {
				purchase.put("defitem8", getContract_name(parent.getDefitem8()));// ��ͬ����
			}
			purchase.put("defitem9", getInvoice_name(parent.getDefitem9()));// ��ͬ����
			purchase.put("defitem10", getInvoice_name(parent.getDefitem10()));// ��ͬϸ��
			purchase.put("defitem11", parent.getDefitem11());// ��ͬ�ܽ��
			purchase.put("defitem27", parent.getDefitem27());// �ۼ��������
			purchase.put("defitem28", parent.getDefitem28());// �ۼ���֧�����
			purchase.put("defitem30", parent.getDefitem30());// ʣ��������(��̬���)
			purchase.put("SecurityDepositRemainingAmount",
					parent.getDefitem48());// ʣ��������(��֤��Ѻ���)
			purchase.put("defitem38", parent.getDefitem38());// δ֧�����
			purchase.put("defitem35", parent.getDefitem35());// ��֤��/Ѻ����
			purchase.put("defitem36", getInvoice_name(parent.getDefitem36()));// �������
			purchase.put("zdy11", parent.getDefitem39());// ���������
			purchase.put("defitem24", parent.getDefitem24());// �ɹ�������
			purchase.put("defitem13", parent.getDefitem13());// �Ƿ�������Ʊ
			purchase.put("defitem42", parent.getOrg_amount() == null ? 0
					: parent.getOrg_amount().getDouble());// �˴������
			purchase.put("defitem23", parent.getDefitem23());// �ۼƿۿ���
			purchase.put("defitem3", parent.getDefitem3());// Ӱ�����
			purchase.put("defitem4", parent.getDefitem4());// Ӱ��״̬
			purchase.put("reason_summaryname", parent.getReason());// �������
			purchase.put("recaccount", getPsn_accnum(parent.getDefitem21()));// �տ������˺�����
			purchase.put("def32", parent.getDefitem51());// �տ������˻�����
			purchase.put("SmallTaxpayer", parent.getDefitem44());// �Ƿ�С��ģ��˰��
			purchase.put("BusinessMail", parent.getDefitem33());// ҵ��Ա����
			purchase.put("isPeriodicAccrual", parent.getDefitem29());// �Ƿ������Լ���
			// ������Ϣ����
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
			purchaseDetaillist = getLLPurchaseDetaillist(aggvo);
			// ����ƻ�ҳǩ
			List<Map<String, Object>> accruedVerifyList = new ArrayList<>();// ����ƻ�����list
			if (aggvo.getTableVO("mtapp_payplan") != null
					&& aggvo.getTableVO("mtapp_payplan").length > 0) {
				accruedVerifyList = getLLAccruedVerifyList(aggvo);
			}

			Map<String, Object> formData = new HashMap<String, Object>();// ������
			if (billtype.equals("261X-Cxx-LL01")
					|| billtype.equals("261X-Cxx-LL02")) {
				formData.put("I_SupplierContractOrnotLL", purchase);
				formData.put("C_ContractOrnotDetail", purchaseDetaillist);
				formData.put("C_ContractPaymentPlanLL", accruedVerifyList);
			}

			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			String ProcessName = getIUAPQueryBS("SELECT exsysdes FROM xx_bdcontra_b where bdcode = '"
					+ billtype + "'");// ����������ϸ����
			if (("261X-Cxx-LL01").equals(billtype)
					|| ("261X-Cxx-LL02").equals(billtype)) {
				postdata.put("ProcessName", ProcessName);
				postdata.put("ExistTaskID", bpmid);
				postdata.put("FormData", formData);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("usercode", creator);
			}

			ResultVO vo = TGCallUtils.getUtils().onDesCallService(
					parent.getPrimaryKey(), "BPM", "PostTask", postdata);
			return vo;
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		} finally {
			// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
			PKLock.getInstance().releaseLock(lock, null, null);
		}

	}

	/**
	 * �����ϸ
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(
			AggMatterAppVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		MtAppDetailVO[] detailvos = aggvo.getChildrenVO();
		for (MtAppDetailVO vo : detailvos) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
			purchaseDetail.put("BudgetCode", getObjcode(vo.getDefitem12()));// Ԥ���Ŀ����
			purchaseDetail.put("defitem30", vo.getDefitem6());// Ԥ���Ŀȫ��
			purchaseDetail.put("orig_amount", vo.getDefitem13());// Ԥ����
			purchaseDetail.put("pk_project_project_name",
					getProject_name(vo.getPk_project()));// ��Ŀ����
			purchaseDetail.put("defitem2", getInvoice_name(vo.getDefitem10()));// ��Ʊ����
			purchaseDetail.put("Invoiceamount", vo.getDefitem23());// ��Ʊ���
			purchaseDetail.put("defitem8", vo.getDefitem8());// ����˰���
			purchaseDetail.put("defitem7", vo.getDefitem7());// ˰��%
			purchaseDetail.put("defitem12", vo.getDefitem30());// ˰��
			purchaseDetail.put("defitem13", vo.getOrig_amount() == null ? ""
					: vo.getOrig_amount().getDouble());// ���������
			purchaseDetail.put("def3", vo.getDefitem29());// �ɵֿ�˰��
			purchaseDetail.put("Fundtype", getInvoice_name(vo.getDefitem16()));// ��������
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	/**
	 * ����ƻ�
	 * 
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	private List<Map<String, Object>> getLLAccruedVerifyList(
			AggMatterAppVO aggvo) throws BusinessException {
		List<Map<String, Object>> accruedVerifyList = new ArrayList<>();
		MtAppPayplanVO[] payplans = (MtAppPayplanVO[]) aggvo
				.getTableVO("mtapp_payplan");
		for (MtAppPayplanVO vo : payplans) {
			Map<String, Object> accruedDetail = new HashMap<String, Object>();
			accruedDetail.put("paymentType", getInvoice_name(vo.getDefitem2()));// ��������
			accruedDetail.put("fundtype", getInvoice_name(vo.getDefitem1()));// ��������
			accruedDetail.put("planpaydate", vo.getPlanpaydate() == null ? ""
					: vo.getPlanpaydate().toString());// �ƻ���������
			accruedDetail.put("payrate", vo.getPayrate() == null ? 0 : vo
					.getPayrate().getDouble());// �������%
			accruedDetail.put("planpaymny", vo.getPlanpaymny() == null ? 0 : vo
					.getPlanpaymny().getDouble());// �ƻ�������
			accruedDetail.put("paycondit", vo.getPaycondit());// ��������
			accruedDetail.put("offsetno", vo.getOffsetno());// �ֳ嵥��
			accruedDetail.put("offsetmny", vo.getOffsetmny() == null ? 0 : vo
					.getOffsetmny().getDouble());// �ֳ���
			accruedDetail.put("totalpaymny", vo.getTotalpaymny() == null ? 0
					: vo.getTotalpaymny().getDouble());// �ۼ��Ѹ����
			accruedDetail.put("thisRequestAmount", vo.getApplymny() == null ? 0
					: vo.getApplymny().getDouble());// ���������
			accruedVerifyList.add(accruedDetail);
		}
		return accruedVerifyList;
	}

	public String getIUAPQueryBS(String sql) {
		String result = "";
		try {
			result = (String) getQueryBS().executeQuery(sql,
					new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}

	private IUAPQueryBS getQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	/**
	 * ��Ʊ��������
	 * 
	 * @param pk_invoice
	 * @return
	 */
	public String getInvoice_name(String pk_invoice) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_invoice + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ŀ����
	 * 
	 * @param jobid
	 * @return
	 */
	public String getProject_name(String pk_project) {
		String sql = "select b.project_name from bd_project b where b.pk_project='"
				+ pk_project + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��ͬ����
	 * 
	 * @param pk_fct_ap
	 * @return
	 */
	public String getContract_name(String pk_fct_ap) {
		String sql = "SELECT ap.ctname FROM fct_ap ap where ap.blatest = 'Y' and ap.dr = 0 and ap.pk_fct_ap = '"
				+ pk_fct_ap + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	public String getPk_org_v_name(String pk_org_v) {
		String sql = "select o.name, o.code from org_financeorg_v o where o.pk_vid='"
				+ pk_org_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ӧ������
	 * 
	 * @param pk_supplier
	 * @return
	 */
	public String getPk_supplier_name(String pk_supplier) {
		String sql = "	select b.name from bd_supplier b where b.pk_supplier = '"
				+ pk_supplier + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��������Ϣ
	 * 
	 * @param cuserid
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getApplyer_info(String cuserid)
			throws BusinessException {
		String sql = "	select b.user_name,b.user_code from sm_user b where b.pk_psndoc = '"
				+ cuserid + "'";
		@SuppressWarnings("unchecked")
		Map<String, String> info = (Map<String, String>) getQueryBS()
				.executeQuery(sql, new MapProcessor());
		if (info == null) {
			info = new HashMap<String, String>();
		}
		return info;
	}

	/**
	 * ������������֯��Ϣ
	 * 
	 * @param cuserid
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getOrg_info(String pk_org)
			throws BusinessException {
		String sql = "SELECT org.name,org.code FROM org_orgs org where org.pk_org = '"
				+ pk_org + "'";
		@SuppressWarnings("unchecked")
		Map<String, String> info = (Map<String, String>) getQueryBS()
				.executeQuery(sql, new MapProcessor());
		if (info == null) {
			info = new HashMap<String, String>();
		}
		return info;
	}

	/**
	 * ����������������Ϣ
	 * 
	 * @param cuserid
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDept_info(String pk_dept)
			throws BusinessException {
		String sql = "SELECT dept.name,dept.code FROM org_dept dept where dept.pk_dept = '"
				+ pk_dept + "'";
		@SuppressWarnings("unchecked")
		Map<String, String> info = (Map<String, String>) getQueryBS()
				.executeQuery(sql, new MapProcessor());
		if (info == null) {
			info = new HashMap<String, String>();
		}
		return info;
	}

	/**
	 * ���������˺�
	 * 
	 * @param skyhzh
	 * @return
	 */
	public String getPsn_accnum(String pk_bankaccsub) {
		String sql = "select b.accname from bd_bankaccsub b where b.pk_bankaccsub='"
				+ pk_bankaccsub + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˻������˻�
	 * 
	 * @param custaccount
	 * @return
	 */
	public String getCustaccount_accnum(String custaccount) {
		String sql = "select b.accnum from bd_bankaccsub b where b.pk_bankaccsub = '"
				+ custaccount + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �Ƶ����û�����
	 */
	public String getUser_code(String cuserid) {
		String sql = "select s.user_code from sm_user s where s.cuserid='"
				+ cuserid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ŀ����
	 * 
	 * @param jobid
	 * @return
	 */
	public String getPproject_name(String jobid) {
		String sql = "select b.project_name from bd_project b where b.pk_project='"
				+ jobid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	public String getPostcode(String zyx1) {
		String sql = "select o.postcode from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��������
	 * 
	 * @param bzbm
	 * @return
	 */
	public String getBzbm(String bzbm) {
		String sql = "select b.name from bd_currtype b where b.pk_currtype = '"
				+ bzbm + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������
	 * 
	 * @param zyx6
	 * @return
	 */
	public String getPlatename(String zyx6) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx6 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	public String getPk_org_v_code(String pk_org_v) {
		String sql = "select o.code, o.code from org_financeorg_v o where o.pk_vid='"
				+ pk_org_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������λ����
	 * 
	 * @param fydwbm_v
	 * @return
	 */
	public String getFydwbm_v_name(String fydwbm_v) {
		String sql = "select o.name from org_orgs_v o where o.pk_vid = '"
				+ fydwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������λ����
	 * 
	 * @param fydwbm_v
	 * @return
	 */
	public String getFydwbm_v_code(String fydwbm_v) {
		String sql = "select o.code from org_orgs_v o where o.pk_vid = '"
				+ fydwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ�������������
	 * 
	 * @param fydeptid_v
	 * @return
	 */
	public String getFydeptid_v_name(String fydeptid_v) {
		String sql = "select o.name from org_dept_v o where o.pk_vid = '"
				+ fydeptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ��������ű���
	 * 
	 * @param fydeptid_v
	 * @return
	 */
	public String getFydeptid_v_code(String fydeptid_v) {
		String sql = "select o.code from org_dept_v o where o.pk_vid = '"
				+ fydeptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˵�λ����
	 * 
	 * @param dwbm_v
	 * @return
	 */
	public String getDwbm_v_name(String dwbm_v) {
		String sql = "select o.name from org_adminorg_v o where o.pk_vid='"
				+ dwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˵�λ����
	 * 
	 * @param dwbm_v
	 * @return
	 */
	public String getDwbm_v_code(String dwbm_v) {
		String sql = "select o.code from org_adminorg_v o where o.pk_vid='"
				+ dwbm_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˲�������
	 * 
	 * @param deptid_v
	 * @return
	 */
	public String getDeptid_v_name(String deptid_v) {
		String sql = "select o.name from org_dept_v o where o.pk_vid='"
				+ deptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��ȡHRϵͳdeptid
	 * 
	 * @param deptid
	 * @return
	 */
	public String getDeptID(String postcode) {
		String sql = "select c.departmentid from positioitem c where c.seqnum='"
				+ postcode + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˲��ű���
	 * 
	 * @param deptid_v
	 * @return
	 */
	public String getDeptid_v_code(String deptid_v) {
		String sql = "select o.code from org_dept_v o where o.pk_vid='"
				+ deptid_v + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param jkbxr
	 * @return
	 */
	public String getJkbxr_name(String jkbxr) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ jkbxr + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	public String getPostname(String zyx1) {
		String sql = "select o.postname from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����������
	 * 
	 * @param zyx10
	 * @return
	 */
	public String getProcessCategory_name(String zyx10) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx10 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param zyx10
	 * @return
	 */
	public String getProcessCategory_code(String zyx10) {
		String sql = "select b.code from bd_defdoc b where b.pk_defdoc = '"
				+ zyx10 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ӧ������
	 * 
	 * @param hbbm
	 * @return
	 */
	public String getHbbm_name(String hbbm) {
		String sql = "	select b.name from bd_supplier b where b.pk_supplier = '"
				+ hbbm + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����������
	 * 
	 * @param operator
	 * @return
	 */
	public String getOperator_name(String operator) {
		String sql = "select d.name	from sm_user c, bd_psndoc d"
				+ " where c.pk_psndoc = d.pk_psndoc  and c.cuserid = '"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������û�����
	 */
	public String getOperator_code(String operator) {
		String sql = "select s.user_code from sm_user s where s.cuserid='"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �տ�������
	 * 
	 * @param receiver
	 * @return
	 */
	public String getReceiver_name(String receiver) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ receiver + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ˰��
	 * 
	 * @param zyx21
	 * @return
	 */
	public String getTax_rate(String zyx21) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx21 + "'";
		return getIUAPQueryBS(sql);
	}

	// ===========================================================================================
	/**
	 * Ԥ���Ŀ����
	 * 
	 * @param defitem12
	 * @return
	 */
	public String getObjname(Object object) {
		String sql = "select b.objname from tb_budgetsub b where b.pk_obj = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * Ԥ���Ŀ����
	 * 
	 * @param defitem12
	 * @return
	 */
	public String getObjcode(Object object) {
		String sql = "select b.objcode from tb_budgetsub b where b.pk_obj = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ������������
	 * 
	 * @param defitem20
	 * @return
	 */
	public String getCosttype_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ҵ̬ά����
	 * 
	 * @param defitem22
	 * @return
	 */
	public String getYetai_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���Ʋ�������
	 * 
	 * @param defitem26
	 * @return
	 */
	public String getPlatedept_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����¥������
	 * 
	 * @param defitem24
	 * @return
	 */
	public String getFloordept_name(Object defitem24) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ defitem24 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���������˺�
	 * 
	 * @param skyhzh
	 * @return
	 */
	public String getSkyhzh_accnum(String skyhzh) {
		String sql = "select b.accnum from bd_bankaccsub b where b.pk_bankaccsub='"
				+ skyhzh + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �ͻ�����
	 * 
	 * @param customer
	 * @return
	 */
	public String getCustomer_name(String customer) {
		String sql = "select b.name from bd_customer b where b.pk_customer='"
				+ customer + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��֧��Ŀ����
	 * 
	 * @param szxmid
	 * @return
	 */
	public String getSzxmid_name(String szxmid) {
		String sql = "select b.name from bd_inoutbusiclass b where b.pk_inoutbusiclass='"
				+ szxmid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ʊ��������
	 * 
	 * @param defitem15
	 * @return
	 */
	public String getInvoice_name(Object defitem15) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * org_orgs��֯����
	 * 
	 * @param defitem15
	 * @return
	 */
	public String getOrg_orgs_name(Object defitem15) {
		String sql = "select name from org_orgs o where o.pk_org = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * org_dept��������
	 * 
	 * @param defitem15
	 * @return
	 */
	public String getOrg_dept_name(Object defitem15) {
		String sql = "select name from org_dept o where o.pk_dept = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}

	private String getJsfs(String key, String pk_balatype) {
		String sql = "SELECT " + key
				+ " FROM bd_balatype WHERE pk_balatype = '" + pk_balatype
				+ "' AND enablestate = 2 AND NVL(dr,0)=0";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �û�����
	 * 
	 * @param bzbm
	 * @return
	 */
	public String getSmUserName(String cuserid) {
		String sql = "select b.user_name from sm_user b where b.cuserid = '"
				+ cuserid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ����TaskId
	 * 
	 * @param
	 * @return
	 */
	public void updateTaskID(String zyx30, String pk_jkbx) {
		BaseDAO dao = new BaseDAO();
		String sql = "update er_bxzb e  set e.zyx29 = 'Y',e.zyx30 = '" + zyx30
				+ "' where e.pk_jkbx = '" + pk_jkbx + "'";
		try {
			dao.executeUpdate(sql);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public List<FileManageVO> fileManageVOs(String pk_jkbx) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String sql = "select b.* from bd_filemanage b, sm_pub_filesystem s "
				+ "where s.pk = b.pk_filemanage and s.isfolder = 'n' and  s.filepath like '"
				+ pk_jkbx + "%'";
		List<FileManageVO> listVos = new ArrayList<FileManageVO>();
		try {
			listVos = (List<FileManageVO>) bs.executeQuery(sql,
					new BeanListProcessor(FileManageVO.class));
			if (listVos != null && listVos.size() > 0) {
				return listVos;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 2018-08-16 linhs
	 * 
	 * @param ��������
	 * @return
	 */
	private List<Map<String, Object>> getFiles(String pk_jkbx) {
		List<FileManageVO> listVOs = fileManageVOs(pk_jkbx);
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (listVOs != null && listVOs.size() > 0) {
			for (FileManageVO vo : listVOs) {
				Map<String, Object> fileMap = new HashMap<String, Object>();
				String sizeSql = "select filelength from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String namesql = "select filepath from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String file_id = vo.getDocument_name() + "&" + vo.getFile_id();
				String name = getIUAPQueryBS(namesql).substring(
						getIUAPQueryBS(namesql).lastIndexOf("/") + 1);
				fileMap.put("FileID", file_id);
				fileMap.put("Name", name);
				fileMap.put("Ext", name.substring(name.lastIndexOf(".")));
				fileMap.put("Size", getIUAPQueryBS(sizeSql) == null ? 0
						: getIUAPQueryBS(sizeSql));
				fileMap.put("AppKey", "NC");
				fileMap.put("AppKeyTicket", "DF6AF297");
				listMap.add(fileMap);
			}
			return listMap;
		}
		return null;
	}
}
