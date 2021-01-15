package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uap.distribution.util.StringUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.push.QueryDocInfoUtils;
import nc.bs.tg.outside.salebpm.utils.SaleBPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.bs.uap.lock.PKLock;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pnt.vo.FileManageVO;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.yer.AddBillHVO;
import nc.vo.yer.AggAddBillHVO;
import nc.vo.yer.InvTypeBVO;
import nc.vo.yer.yer_fillbill.ApplyDetailBVO;
/**
 * ��Ʊ�����Ƶ����ύ����BPM
 * @author ln
 *
 */
public class AddBillPushtoBpmUtil extends SaleBPMBillUtils{
	static AddBillPushtoBpmUtil utils;

	public static AddBillPushtoBpmUtil getUtils() {
		if (utils == null) {
			utils = new AddBillPushtoBpmUtil();
		}
		return utils;
	}
	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggAddBillHVO aggVO = (AggAddBillHVO) bill;
		
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID(String.valueOf(aggVO.getParentVO()
				.getAttributeValue("def12")));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		ISaleBPMBillCont.getBillNameMap().get(billCode);
		Map<String, String> infoMap = SalePushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						ISaleBPMBillCont.getBillNameMap().get(billCode)+"@@@", deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def18"));
		aggVO.getParentVO().setAttributeValue("def18", infoMap.get("taskID"));//BPM����
		aggVO.getParentVO().setAttributeValue("def25", infoMap.get("OpenUrl"));//BPMurl��ַ
		String pushFlag = "N";//����BPM��־
		if("true".equals(infoMap.get("success")))
			pushFlag = "Y";
		aggVO.getParentVO().setAttributeValue("def19", pushFlag);
		return aggVO;
	}
	/**
	 * ��Ϣת��
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggAddBillHVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = getAddBillsInfo(aggVO);
		return formData;
	}
	/**
	 * ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getAddBillsInfo(AggAddBillHVO aggVO)
			throws BusinessException {

		String lock = aggVO.getParentVO().getPrimaryKey() + "&ERM";
		// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("�����ύ��BPM,��ȴ�BPM��Ϣ����!");
		}
		try {
			AddBillHVO parent = aggVO.getParentVO();
			Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
//			purchase.put("SerialNumber", parent.getBillno());//��ˮ��
			purchase.put("Title", parent.getTitle());//����
			//add by tjl 2020-04-24
			String pk_org = parent.getPk_org();
			String easy = null;
			if(StringUtils.isNotBlank(pk_org)){
				Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
				if(orgsInfoMap!=null){
					easy = orgsInfoMap.get("def11");
				}
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}
			//end
			purchase.put("Applicant", getJkbxr_name(parent.getPk_busiman()));//������
			String sql = "select b.code from bd_psndoc b where b.pk_psndoc='"+parent.getPk_busiman()+"'";
			String result = getIUAPQueryBS(sql);
			purchase.put("ApplicantCode", result);//�������˺�
			purchase.put("ApplicationDate", parent.getBilldate().toString());//��������
			sql = "SELECT NAME FROM org_adminorg WHERE pk_adminorg = '"+parent.getDef11()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationCompany", result);//���빫˾
			sql = "SELECT code FROM org_adminorg WHERE pk_adminorg = '"+parent.getDef11()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationCompanyCode", result);//���빫˾����
			sql = "SELECT NAME FROM org_dept WHERE pk_dept = '"+parent.getPk_dept()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationDepartment", result);//���벿��
			sql = "SELECT code FROM org_dept WHERE pk_dept = '"+parent.getPk_dept()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationDepartmentCode", result);//���벿�Ŵ���
			sql = "select name from bd_defdoc where pk_defdoc = '"+parent.getDef24()+"'";
			result = getIUAPQueryBS(sql);
			purchase.put("FareAdjustmentType", result);//��Ʊ����
			sql = "SELECT NAME FROM org_orgs WHERE pk_org = '"+parent.getPk_org()+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchase.put("AccountingCompany", result);//���˹�˾
			purchase.put("RequestMoneyNo", parent.getBillno());//����
			purchase.put("RequestMoneyAmount", "");//�����
			purchase.put("ContractNo", parent.getConcode());//��ͬ����
			purchase.put("ContractName", getContract_name(parent.getDef21()));//��ͬ����
			purchase.put("ContractTotalPaymentAmount", parent.getTotalpaymoney());//��ͬ�ۼ��Ѹ����
			purchase.put("TotalInvoiceAmount", parent.getDef20());//�ۼƷ�Ʊ���
			purchase.put("BarCode", parent.getImagcode());//Ӱ�����
			purchase.put("ImageStatus", parent.getImagstatus());//Ӱ��״̬
			purchase.put("Completed", parent.getDef23());//�Ѳ�ȫ
			purchase.put("SupplementOrNot", parent.getDef22());//������
			purchase.put("Description", parent.getNotes());//��Ʊ˵��
//			purchase.put("total", parent.getAmount() == null ? 0 : parent
//					.getAmount().getDouble());

			// ������Ϣ����
			List<Map<String, Object>> applyDetaillist = new ArrayList<>();// �����ϸ����list
			applyDetaillist = getApplyDetaillist(aggVO);
			List<Map<String, Object>> invoiceDetaillist = new ArrayList<>();// ��Ʊ��ϸ����list
			invoiceDetaillist = getInvoiceDetaillist(aggVO);

			Map<String, Object> formData = new HashMap<String, Object>();// ������
			formData.put("I_OnlyTicketFK", purchase);
			formData.put("C_OnlyTicketFKAsk_Detail", applyDetaillist);
			formData.put("C_OnlyTicketFKInvoice_Detail", invoiceDetaillist);
			return formData;

		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new LfwRuntimeException("��" + e.getMessage() + "��");
		} finally {
			// add by huangdq 2019-09-11 ����BPM����������,�����ظ���������
			 PKLock.getInstance().releaseLock(lock, null, null);
		}
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
	private List<FileManageVO> fileManageVOs(String pk_jkbx) {
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
	 * ��ȡ��Ʊ��ϸ����
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getInvoiceDetaillist(AggAddBillHVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		String sql = null;
		String result = null;
		for (CircularlyAccessibleValueObject vo : aggvo.getTableVO("id_invtypebvo")) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
			sql = "SELECT objname FROM tb_budgetsub WHERE pk_obj = '"+vo.getAttributeValue(InvTypeBVO.DEF2)+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("BudgetSubject",  result);//Ԥ���Ŀ
			sql = "select name from bd_defdoc where pk_defdoc = '"+vo.getAttributeValue(InvTypeBVO.DEF1)+"';";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("InvoiceType",  result);//��Ʊ����
			purchaseDetail.put("InvoiceAmount",  vo.getAttributeValue(InvTypeBVO.MONEY));//��Ʊ���
			purchaseDetail.put("TaxRate",  vo.getAttributeValue(InvTypeBVO.RATE));//˰��
			purchaseDetail.put("TaxAmount",  vo.getAttributeValue(InvTypeBVO.TAX));//˰��
			sql = "SELECT NAME FROM bd_supplier WHERE pk_supplier = '"+vo.getAttributeValue(InvTypeBVO.DEF10)+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("Supplier",  result);//��Ӧ��
			purchaseDetail.put("PaymentMethod",  "");//���㷽ʽ
			purchaseDetail.put("ReceiptPriority",  "");//�������ȼ�
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}
	/**
	 * ��ȡ�����ϸ����
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getApplyDetaillist(AggAddBillHVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// ��������list
		for (CircularlyAccessibleValueObject vo : aggvo.getTableVO("id_reqdetailbvo")) {
			String billno = (String) vo.getAttributeValue(ApplyDetailBVO.DEF3);
//			if(StringUtil.isBlank(billno)){
//				throw new LfwRuntimeException("������ʽΪ�Ǻ�ͬ���ʱ�����Ų���Ϊ��!��");
//			}
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// ��������
			purchaseDetail.put("RequestNo",  billno);//����
			purchaseDetail.put("RequestAmount",  vo.getAttributeValue(ApplyDetailBVO.APPLYMNY));//�����
			purchaseDetail.put("ReturnTicketAmount",  vo.getAttributeValue(ApplyDetailBVO.MONEY));//��Ʊ���
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}
	private String getIUAPQueryBS(String sql) {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String result = "";
		try {
			result = (String) bs.executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * �������
	 * 
	 * @param zyx6
	 * @return
	 */
	private String getPlatename(String zyx6) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ zyx6 + "'";
		return getIUAPQueryBS(sql);
	}
	
	/**
	 * ��������
	 * 
	 * @param bzbm
	 * @return
	 */
	private String getBzbm(String bzbm) {
		String sql = "select b.name from bd_currtype b where b.pk_currtype = '"
				+ bzbm + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	private String getPk_org_v_name(String pk_org_v) {
		String sql = "select o.name, o.code from org_financeorg_v o where o.pk_vid='"
				+ pk_org_v + "'";
		return getIUAPQueryBS(sql);
	}
	/**
	 * ���˹�˾����
	 * 
	 * @param pk_org_v
	 * @return
	 */
	private String getPk_org_v_code(String pk_org_v) {
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
	private String getFydwbm_v_name(String fydwbm_v) {
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
	private String getFydwbm_v_code(String fydwbm_v) {
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
	private String getFydeptid_v_name(String fydeptid_v) {
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
	private String getFydeptid_v_code(String fydeptid_v) {
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
	private String getDwbm_v_name(String dwbm_v) {
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
	private String getDwbm_v_code(String dwbm_v) {
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
	private String getDeptid_v_name(String deptid_v) {
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
	private String getDeptID(String postcode) {
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
	private String getDeptid_v_code(String deptid_v) {
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
	private String getJkbxr_name(String jkbxr) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ jkbxr + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������û�����
	 */
	private String getUser_code(String pk_psndoc) {
		String sql = "select s.user_code from sm_user s where s.pk_psndoc='"
				+ pk_psndoc + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	private String getPostname(String zyx1) {
		String sql = "select o.postname from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��λ����
	 * 
	 * @param zyx1
	 * @return
	 */
	private String getPostcode(String zyx1) {
		String sql = "select o.postcode from om_post o where o.pk_post = '"
				+ zyx1 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��ͬ����
	 * 
	 * @param zyx6
	 * @return
	 */
	private String getContract_name(String zyx9) {
		String sql = "select b.name from yer_contractfile b where b.pk_defdoc = '"
				+ zyx9 + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����������
	 * 
	 * @param zyx10
	 * @return
	 */
	private String getProcessCategory_name(String zyx10) {
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
	private String getProcessCategory_code(String zyx10) {
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
	private String getHbbm_name(String hbbm) {
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
	private String getOperator_name(String operator) {
		String sql = "select d.name	from sm_user c, bd_psndoc d"
				+ " where c.pk_psndoc = d.pk_psndoc  and c.cuserid = '"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �������û�����
	 */
	private String getOperator_code(String operator) {
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
	private String getReceiver_name(String receiver) {
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
	private String getTax_rate(String zyx21) {
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
	private String getObjname(Object object) {
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
	private String getObjcode(Object object) {
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
	private String getCosttype_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��Ŀ����
	 * 
	 * @param jobid
	 * @return
	 */
	private String getPproject_name(String jobid) {
		String sql = "select b.project_name from bd_project b where b.pk_project='"
				+ jobid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ҵ̬ά����
	 * 
	 * @param defitem22
	 * @return
	 */
	private String getYetai_name(Object object) {
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
	private String getPlatedept_name(Object object) {
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
	private String getFloordept_name(Object defitem24) {
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
	private String getSkyhzh_accnum(String skyhzh) {
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
	private String getCustomer_name(String customer) {
		String sql = "select b.name from bd_customer b where b.pk_customer='"
				+ customer + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * �����˻������˻�
	 * 
	 * @param custaccount
	 * @return
	 */
	private String getCustaccount_accnum(String custaccount) {
		String sql = "select b.accnum from bd_bankaccsub b where b.pk_bankaccsub = '"
				+ custaccount + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * ��֧��Ŀ����
	 * 
	 * @param szxmid
	 * @return
	 */
	private String getSzxmid_name(String szxmid) {
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
	private String getInvoice_name(Object defitem15) {
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
	private String getOrg_orgs_name(Object defitem15) {
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
	private String getOrg_dept_name(Object defitem15) {
		String sql = "select name from org_dept o where o.pk_dept = '"
				+ defitem15 + "'";
		return getIUAPQueryBS(sql);
	}
}
