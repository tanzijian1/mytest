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
	 * 组装报销单数据
	 * 
	 * @param billtype
	 * @param jkbxvo
	 * @return
	 * @throws BusinessException
	 */
	public ResultVO pushLLBillToBpm(String billtype, JKBXVO jkbxvo)
			throws BusinessException {
		String lock = jkbxvo.getParentVO().getPrimaryKey() + "&ERM";
		// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("表单已提交至BPM,请等待BPM信息反馈!");
		}
		try {
			JKBXHeaderVO parent = jkbxvo.getParentVO();
			String bpmid = parent.getZyx30();
			String postcode = getPostcode(parent.getZyx1());
			String deptid = null;
			if (postcode != null) {
				deptid = getDeptID(postcode);
			}
			Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
			purchase.put("Title", parent.getZyx22());// 标题
			purchase.put("Applicant", getOperator_name(parent.getOperator()));// 申请人
			purchase.put("ApplicantCode",
					getOperator_code(parent.getOperator()));// 申请人账号
			purchase.put("ApplicationDate", parent.getDjrq().toString());// 申请日期
			purchase.put("ApplicationCompany",
					getDwbm_v_name(parent.getDwbm_v()));// 申请公司
			purchase.put("ApplicationCompanyCode",
					getDwbm_v_code(parent.getDwbm_v()));// 申请公司编码
			purchase.put("ApplicationDepartment",
					getDeptid_v_name(parent.getDeptid_v()));// 申请部门
			purchase.put("ApplicationDepartmentCode",
					getDeptid_v_code(parent.getDeptid_v()));// 申请部门编码
			purchase.put("djbh", parent.getDjbh());// NC单据编号
			purchase.put("pk_org_v_name",
					getPk_org_v_name(parent.getPk_org_v()));// 出账公司
			purchase.put("djrq", parent.getDjrq().toString());// 单据日期
			purchase.put("total", parent.getTotal() == null ? 0 : parent
					.getTotal().getDouble());// 申请金额
			purchase.put("zyx2", parent.getZyx2());// 合同编码
			purchase.put("zyx9", getContract_name(parent.getZyx9()));// 合同名称
			purchase.put("paytarget", parent.getPaytarget() == 0 ? "员工"
					: parent.getPaytarget() == 1 ? "供应商" : "客户");// 收款对象
			purchase.put("receiver_name",
					getReceiver_name(parent.getReceiver()));// 收款人
			purchase.put("skyhzh_accname", getSkyhzh_accnum(parent.getSkyhzh()));// 收款账户
			purchase.put("zyx50", parent.getZyx50());// 开户行
			purchase.put("zyx16", parent.getZyx16());// 影像编码
			purchase.put("def67", parent.getDef67());// 请款事由

			String jsfsName = getJsfs("name", parent.getJsfs());
			purchase.put("jsfs_name", jsfsName);// 结算方式
			purchase.put("fydwbm_name", getFydwbm_v_name(parent.getFydwbm_v()));// 预算归属单位

			// 表体信息处理
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
			purchaseDetaillist = getLLPurchaseDetaillist(jkbxvo);
			// 预提信息处理
			List<Map<String, Object>> accruedVerifyList = new ArrayList<>();// 预提信息数据list
			if (jkbxvo.getAccruedVerifyVO().length != 0) {
				accruedVerifyList = getLLAccruedVerifyList(jkbxvo);
			}

			Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
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

			Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
			String ProcessName = getIUAPQueryBS("SELECT exsysdes FROM xx_bdcontra_b where bdcode = '"
					+ billtype + "'");// 基础数据详细对照
			if (billtype.equals("264X-Cxx-LL01")
					|| billtype.equals("264X-Cxx-LL02")
					|| billtype.equals("264X-Cxx-LL03")) {
				postdata.put("ProcessName", ProcessName);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
			// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
			PKLock.getInstance().releaseLock(lock, null, null);
		}
	}

	/**
	 * 获取邻里单据明细数据
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(JKBXVO jkbxvo) {
		String djlxbm = jkbxvo.getParentVO().getDjlxbm();
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		for (BXBusItemVO vos : jkbxvo.getChildrenVO()) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据

			purchaseDetail.put("defitem48", vos.getDefitem48());// 预算全称
			purchaseDetail.put("jobid_project_name",
					getPproject_name(vos.getJobid()));// 项目
			purchaseDetail.put("jkbxr_name", getJkbxr_name(vos.getJkbxr()));// 用款人
			purchaseDetail
					.put("defitem15", getInvoice_name(vos.getDefitem15()));// 发票类型
			purchaseDetail.put("defitem18", vos.getDefitem18());// 发票号
			purchaseDetail.put("defitem23", vos.getDefitem23());// 发票金额
			purchaseDetail.put("ybje", vos.getYbje() == null ? 0 : vos
					.getYbje().getDouble());// 原币金额
			purchaseDetail.put("defitem16", vos.getDefitem16());// 税率
			purchaseDetail.put("defitem13", vos.getDefitem13());// 不含税金额
			purchaseDetail.put("defitem14", vos.getDefitem14());// 税额
			purchaseDetail.put("defitem30", vos.getDefitem30());// 备注
			purchaseDetail.put("amount", vos.getAmount() == null ? 0 : vos
					.getAmount().getDouble());// 请款金额
			if ("264X-Cxx-LL02".equals(djlxbm)
					|| "264X-Cxx-LL03".equals(djlxbm)) {
				if ("264X-Cxx-LL02".equals(djlxbm)) {
					purchaseDetail.put("defitem1", vos.getDefitem1());// 出发日期
					purchaseDetail.put("defitem2", vos.getDefitem2());// 结束日期
					purchaseDetail.put("defitem3", vos.getDefitem3());// 出发地点
					purchaseDetail.put("defitem4", vos.getDefitem4());// 到达地点
				}
				purchaseDetail.put("defitem20",
						getCosttype_name(vos.getDefitem20()));// 费用类型
			}

			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	/**
	 * 获取邻里预提冲销数据
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
			// 报销核销预提明细 er_accrued_verify
			accruedDetail.put("accrued_billno", accVerVo.getAccrued_billno());// 预提单据编号
			accruedDetail.put("verify_amount",
					accVerVo.getVerify_amount() == null ? 0 : accVerVo
							.getVerify_amount().getDouble());// 核销金额

			accruedDetail.put("verify_man_user_name",
					getSmUserName(accVerVo.getVerify_man()));// 核销人
			// accruedDetail.put("defitem12", accVerVo.getVerify_amount());//
			// accruedDetail.put("defitem30", accVerVo.get);//

			accruedVerifyList.add(accruedDetail);
		}
		return accruedVerifyList;
	}

	/**
	 * 组装费用申请单数据推送到BPM
	 * 
	 * @param billtype
	 *            交易类型
	 * @param aggvo
	 *            AggMatterAppVO
	 * @throws BusinessException
	 */
	public ResultVO pushMattBillToBpm(String billtype, AggMatterAppVO aggvo)
			throws BusinessException {
		String lock = aggvo.getParentVO().getPrimaryKey() + "&BPM";
		// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("表单已提交至BPM,请等待BPM信息反馈!");
		}
		try {
			MatterAppVO parent = aggvo.getParentVO();
			String creator = getUser_code(parent.getCreator());
			String bpmid = parent.getDefitem1();

			Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据

			Map<String, String> map = getApplyer_info(parent.getBillmaker());
			purchase.put("Title", parent.getDefitem45());// 标题
			purchase.put("Applicant", map.get("user_name"));// 申请人
			purchase.put("ApplicantCode", map.get("user_code"));// 申请人账号
			purchase.put("ApplicationDate", parent.getBilldate().toString());// 申请日期

			map = getOrg_info(parent.getApply_org());
			purchase.put("ApplicationCompany", map.get("name"));// 申请公司
			purchase.put("ApplicationCompanyCode", map.get("code"));// 申请公司编码

			map = getDept_info(parent.getApply_dept());
			purchase.put("ApplicationDepartment", map.get("name"));// 申请部门
			purchase.put("ApplicationDepartmentCode", map.get("code"));// 申请部门编码
			purchase.put("djbh", parent.getBillno());// NC单据编号
			purchase.put("pk_org_v_name",
					getPk_org_v_name(parent.getPk_org_v()));// 出账公司名称
			purchase.put("pk_org_v_code",
					getPk_org_v_code(parent.getPk_org_v()));// 出账公司编码
			purchase.put("djrq", parent.getBilldate().toString());// 单据日期
			String def15 = parent.getDefitem15();
			String sql = " select code  " + "  from bd_defdoc  "
					+ " where pk_defdoc = '" + def15 + "'";
			String paytarget = getIUAPQueryBS(sql);
			if (!StringUtil.isEmpty(paytarget)) {
				purchase.put(
						"defitem15",
						Integer.valueOf(paytarget) == 0 ? "员工" : Integer
								.valueOf(paytarget) == 1 ? "供应商" : "客户");// 收款对象
			}

			map = getApplyer_info(parent.getDefitem16());
			purchase.put("defitem16", map.get("user_name"));// 收款人
			purchase.put("defitem18", parent.getDefitem18());// 个人银行账户
			purchase.put("pk_supplier",
					getPk_supplier_name(parent.getPk_supplier()));// 供应商
			purchase.put("defitem19", parent.getDefitem19());// 收款银行开户行名称
			purchase.put("defitem20", parent.getDefitem20());// 收款银行开户行编码
			purchase.put("defitem21",
					getCustaccount_accnum(parent.getDefitem21()));// 收款银行账号
			purchase.put("defitem14", parent.getDefitem14());// 是否预付
			purchase.put("iscostshare", parent.getDefitem50());// 是否分摊合同
			purchase.put("defitem7", parent.getDefitem7());// 合同编号
			if (("261X-Cxx-LL01").equals(billtype)) {
				purchase.put("defitem8", parent.getDefitem8());// 合同名称
			} else if (("261X-Cxx-LL02").equals(billtype)) {
				purchase.put("defitem8", getContract_name(parent.getDefitem8()));// 合同名称
			}
			purchase.put("defitem9", getInvoice_name(parent.getDefitem9()));// 合同类型
			purchase.put("defitem10", getInvoice_name(parent.getDefitem10()));// 合同细类
			purchase.put("defitem11", parent.getDefitem11());// 合同总金额
			purchase.put("defitem27", parent.getDefitem27());// 累计已请款金额
			purchase.put("defitem28", parent.getDefitem28());// 累计已支付金额
			purchase.put("defitem30", parent.getDefitem30());// 剩余可请款金额(动态金额)
			purchase.put("SecurityDepositRemainingAmount",
					parent.getDefitem48());// 剩余可请款金额(保证金、押金等)
			purchase.put("defitem38", parent.getDefitem38());// 未支付金额
			purchase.put("defitem35", parent.getDefitem35());// 保证金/押金金额
			purchase.put("defitem36", getInvoice_name(parent.getDefitem36()));// 请款类型
			purchase.put("zdy11", parent.getDefitem39());// 款项可请金额
			purchase.put("defitem24", parent.getDefitem24());// 采购订单号
			purchase.put("defitem13", parent.getDefitem13());// 是否先请款后补票
			purchase.put("defitem42", parent.getOrg_amount() == null ? 0
					: parent.getOrg_amount().getDouble());// 此次请款金额
			purchase.put("defitem23", parent.getDefitem23());// 累计扣款金额
			purchase.put("defitem3", parent.getDefitem3());// 影像编码
			purchase.put("defitem4", parent.getDefitem4());// 影像状态
			purchase.put("reason_summaryname", parent.getReason());// 请款事由
			purchase.put("recaccount", getPsn_accnum(parent.getDefitem21()));// 收款银行账号名称
			purchase.put("def32", parent.getDefitem51());// 收款银行账户编码
			purchase.put("SmallTaxpayer", parent.getDefitem44());// 是否小规模纳税人
			purchase.put("BusinessMail", parent.getDefitem33());// 业务员邮箱
			purchase.put("isPeriodicAccrual", parent.getDefitem29());// 是否周期性计提
			// 表体信息处理
			List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
			purchaseDetaillist = getLLPurchaseDetaillist(aggvo);
			// 付款计划页签
			List<Map<String, Object>> accruedVerifyList = new ArrayList<>();// 付款计划数据list
			if (aggvo.getTableVO("mtapp_payplan") != null
					&& aggvo.getTableVO("mtapp_payplan").length > 0) {
				accruedVerifyList = getLLAccruedVerifyList(aggvo);
			}

			Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
			if (billtype.equals("261X-Cxx-LL01")
					|| billtype.equals("261X-Cxx-LL02")) {
				formData.put("I_SupplierContractOrnotLL", purchase);
				formData.put("C_ContractOrnotDetail", purchaseDetaillist);
				formData.put("C_ContractPaymentPlanLL", accruedVerifyList);
			}

			Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
			String ProcessName = getIUAPQueryBS("SELECT exsysdes FROM xx_bdcontra_b where bdcode = '"
					+ billtype + "'");// 基础数据详细对照
			if (("261X-Cxx-LL01").equals(billtype)
					|| ("261X-Cxx-LL02").equals(billtype)) {
				postdata.put("ProcessName", ProcessName);
				postdata.put("ExistTaskID", bpmid);
				postdata.put("FormData", formData);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
				postdata.put("usercode", creator);
			}

			ResultVO vo = TGCallUtils.getUtils().onDesCallService(
					parent.getPrimaryKey(), "BPM", "PostTask", postdata);
			return vo;
		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		} finally {
			// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
			PKLock.getInstance().releaseLock(lock, null, null);
		}

	}

	/**
	 * 请款明细
	 * 
	 * @param aggvo
	 * @return
	 */
	private List<Map<String, Object>> getLLPurchaseDetaillist(
			AggMatterAppVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		MtAppDetailVO[] detailvos = aggvo.getChildrenVO();
		for (MtAppDetailVO vo : detailvos) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据
			purchaseDetail.put("BudgetCode", getObjcode(vo.getDefitem12()));// 预算科目编码
			purchaseDetail.put("defitem30", vo.getDefitem6());// 预算科目全称
			purchaseDetail.put("orig_amount", vo.getDefitem13());// 预算金额
			purchaseDetail.put("pk_project_project_name",
					getProject_name(vo.getPk_project()));// 项目名称
			purchaseDetail.put("defitem2", getInvoice_name(vo.getDefitem10()));// 发票类型
			purchaseDetail.put("Invoiceamount", vo.getDefitem23());// 发票金额
			purchaseDetail.put("defitem8", vo.getDefitem8());// 不含税金额
			purchaseDetail.put("defitem7", vo.getDefitem7());// 税率%
			purchaseDetail.put("defitem12", vo.getDefitem30());// 税额
			purchaseDetail.put("defitem13", vo.getOrig_amount() == null ? ""
					: vo.getOrig_amount().getDouble());// 本次请款金额
			purchaseDetail.put("def3", vo.getDefitem29());// 可抵扣税额
			purchaseDetail.put("Fundtype", getInvoice_name(vo.getDefitem16()));// 款项性质
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}

	/**
	 * 付款计划
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
			accruedDetail.put("paymentType", getInvoice_name(vo.getDefitem2()));// 款项类型
			accruedDetail.put("fundtype", getInvoice_name(vo.getDefitem1()));// 款项性质
			accruedDetail.put("planpaydate", vo.getPlanpaydate() == null ? ""
					: vo.getPlanpaydate().toString());// 计划付款日期
			accruedDetail.put("payrate", vo.getPayrate() == null ? 0 : vo
					.getPayrate().getDouble());// 付款比例%
			accruedDetail.put("planpaymny", vo.getPlanpaymny() == null ? 0 : vo
					.getPlanpaymny().getDouble());// 计划付款金额
			accruedDetail.put("paycondit", vo.getPaycondit());// 付款条件
			accruedDetail.put("offsetno", vo.getOffsetno());// 抵冲单号
			accruedDetail.put("offsetmny", vo.getOffsetmny() == null ? 0 : vo
					.getOffsetmny().getDouble());// 抵冲金额
			accruedDetail.put("totalpaymny", vo.getTotalpaymny() == null ? 0
					: vo.getTotalpaymny().getDouble());// 累计已付金额
			accruedDetail.put("thisRequestAmount", vo.getApplymny() == null ? 0
					: vo.getApplymny().getDouble());// 本次请款金额
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
	 * 发票类型名称
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
	 * 项目名称
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
	 * 合同名称
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
	 * 出账公司名称
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
	 * 供应商名称
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
	 * 申请人信息
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
	 * 申请人所属组织信息
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
	 * 申请人所属部门信息
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
	 * 个人银行账号
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
	 * 客商账户银行账户
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
	 * 制单人用户编码
	 */
	public String getUser_code(String cuserid) {
		String sql = "select s.user_code from sm_user s where s.cuserid='"
				+ cuserid + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * 项目名称
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
	 * 岗位编码
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
	 * 币种名称
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
	 * 板块名称
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
	 * 出账公司编码
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
	 * 预算归属单位名称
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
	 * 预算归属单位编码
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
	 * 预算归属部门名称
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
	 * 预算归属部门编码
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
	 * 报销人单位名称
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
	 * 报销人单位编码
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
	 * 报销人部门名称
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
	 * 获取HR系统deptid
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
	 * 报销人部门编码
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
	 * 报销人名称
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
	 * 岗位名称
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
	 * 流程类别名称
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
	 * 流程类别编码
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
	 * 供应商名称
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
	 * 申请人名称
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
	 * 申请人用户编码
	 */
	public String getOperator_code(String operator) {
		String sql = "select s.user_code from sm_user s where s.cuserid='"
				+ operator + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * 收款人名称
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
	 * 税率
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
	 * 预算科目名称
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
	 * 预算科目编码
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
	 * 费用类型名称
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
	 * 业态维名称
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
	 * 车牌部门名称
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
	 * 部门楼层名称
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
	 * 个人银行账号
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
	 * 客户名称
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
	 * 收支项目名称
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
	 * 发票类型名称
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
	 * org_orgs组织名称
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
	 * org_dept部门名称
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
	 * 用户名称
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
	 * 更新TaskId
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
	 * @param 附件推送
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
