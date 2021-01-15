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
 * 补票工单制单人提交推送BPM
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
		aggVO.getParentVO().setAttributeValue("def18", infoMap.get("taskID"));//BPM主键
		aggVO.getParentVO().setAttributeValue("def25", infoMap.get("OpenUrl"));//BPMurl地址
		String pushFlag = "N";//推送BPM标志
		if("true".equals(infoMap.get("success")))
			pushFlag = "Y";
		aggVO.getParentVO().setAttributeValue("def19", pushFlag);
		return aggVO;
	}
	/**
	 * 信息转换
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggAddBillHVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		formData = getAddBillsInfo(aggVO);
		return formData;
	}
	/**
	 * 获得传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getAddBillsInfo(AggAddBillHVO aggVO)
			throws BusinessException {

		String lock = aggVO.getParentVO().getPrimaryKey() + "&ERM";
		// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
		boolean ispush = PKLock.getInstance().isLocked(lock, null, null);
		if (ispush) {
			throw new LfwRuntimeException("表单已提交至BPM,请等待BPM信息反馈!");
		}
		try {
			AddBillHVO parent = aggVO.getParentVO();
			Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
//			purchase.put("SerialNumber", parent.getBillno());//流水号
			purchase.put("Title", parent.getTitle());//标题
			//add by tjl 2020-04-24
			String pk_org = parent.getPk_org();
			String easy = null;
			if(StringUtils.isNotBlank(pk_org)){
				Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
				if(orgsInfoMap!=null){
					easy = orgsInfoMap.get("def11");
				}
			}
			if(StringUtils.isNotBlank(easy)&&"简化业财模式".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}
			//end
			purchase.put("Applicant", getJkbxr_name(parent.getPk_busiman()));//申请人
			String sql = "select b.code from bd_psndoc b where b.pk_psndoc='"+parent.getPk_busiman()+"'";
			String result = getIUAPQueryBS(sql);
			purchase.put("ApplicantCode", result);//申请人账号
			purchase.put("ApplicationDate", parent.getBilldate().toString());//申请日期
			sql = "SELECT NAME FROM org_adminorg WHERE pk_adminorg = '"+parent.getDef11()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationCompany", result);//申请公司
			sql = "SELECT code FROM org_adminorg WHERE pk_adminorg = '"+parent.getDef11()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationCompanyCode", result);//申请公司代码
			sql = "SELECT NAME FROM org_dept WHERE pk_dept = '"+parent.getPk_dept()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationDepartment", result);//申请部门
			sql = "SELECT code FROM org_dept WHERE pk_dept = '"+parent.getPk_dept()+"' AND enablestate =2";
			result = getIUAPQueryBS(sql);
			purchase.put("ApplicationDepartmentCode", result);//申请部门代码
			sql = "select name from bd_defdoc where pk_defdoc = '"+parent.getDef24()+"'";
			result = getIUAPQueryBS(sql);
			purchase.put("FareAdjustmentType", result);//补票类型
			sql = "SELECT NAME FROM org_orgs WHERE pk_org = '"+parent.getPk_org()+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchase.put("AccountingCompany", result);//出账公司
			purchase.put("RequestMoneyNo", parent.getBillno());//请款单号
			purchase.put("RequestMoneyAmount", "");//请款金额
			purchase.put("ContractNo", parent.getConcode());//合同编码
			purchase.put("ContractName", getContract_name(parent.getDef21()));//合同名称
			purchase.put("ContractTotalPaymentAmount", parent.getTotalpaymoney());//合同累计已付金额
			purchase.put("TotalInvoiceAmount", parent.getDef20());//累计发票金额
			purchase.put("BarCode", parent.getImagcode());//影像编码
			purchase.put("ImageStatus", parent.getImagstatus());//影像状态
			purchase.put("Completed", parent.getDef23());//已补全
			purchase.put("SupplementOrNot", parent.getDef22());//补附件
			purchase.put("Description", parent.getNotes());//补票说明
//			purchase.put("total", parent.getAmount() == null ? 0 : parent
//					.getAmount().getDouble());

			// 表体信息处理
			List<Map<String, Object>> applyDetaillist = new ArrayList<>();// 请款明细数据list
			applyDetaillist = getApplyDetaillist(aggVO);
			List<Map<String, Object>> invoiceDetaillist = new ArrayList<>();// 发票明细数据list
			invoiceDetaillist = getInvoiceDetaillist(aggVO);

			Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
			formData.put("I_OnlyTicketFK", purchase);
			formData.put("C_OnlyTicketFKAsk_Detail", applyDetaillist);
			formData.put("C_OnlyTicketFKInvoice_Detail", invoiceDetaillist);
			return formData;

		} catch (Exception e) {
			Logger.error(e.getMessage());
			throw new LfwRuntimeException("【" + e.getMessage() + "】");
		} finally {
			// add by huangdq 2019-09-11 增加BPM传参排他锁,避免重复发单问题
			 PKLock.getInstance().releaseLock(lock, null, null);
		}
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
	 * 获取发票明细数据
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getInvoiceDetaillist(AggAddBillHVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		String sql = null;
		String result = null;
		for (CircularlyAccessibleValueObject vo : aggvo.getTableVO("id_invtypebvo")) {
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据
			sql = "SELECT objname FROM tb_budgetsub WHERE pk_obj = '"+vo.getAttributeValue(InvTypeBVO.DEF2)+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("BudgetSubject",  result);//预算科目
			sql = "select name from bd_defdoc where pk_defdoc = '"+vo.getAttributeValue(InvTypeBVO.DEF1)+"';";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("InvoiceType",  result);//发票类型
			purchaseDetail.put("InvoiceAmount",  vo.getAttributeValue(InvTypeBVO.MONEY));//发票金额
			purchaseDetail.put("TaxRate",  vo.getAttributeValue(InvTypeBVO.RATE));//税率
			purchaseDetail.put("TaxAmount",  vo.getAttributeValue(InvTypeBVO.TAX));//税额
			sql = "SELECT NAME FROM bd_supplier WHERE pk_supplier = '"+vo.getAttributeValue(InvTypeBVO.DEF10)+"' AND enablestate = 2 AND NVL(dr,0)=0";
			result = getIUAPQueryBS(sql);
			purchaseDetail.put("Supplier",  result);//供应商
			purchaseDetail.put("PaymentMethod",  "");//结算方式
			purchaseDetail.put("ReceiptPriority",  "");//单据优先级
			purchaseDetaillist.add(purchaseDetail);
		}
		return purchaseDetaillist;
	}
	/**
	 * 获取请款明细数据
	 * 
	 * @param jkbxvo
	 * @return
	 */
	private List<Map<String, Object>> getApplyDetaillist(AggAddBillHVO aggvo) {
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		for (CircularlyAccessibleValueObject vo : aggvo.getTableVO("id_reqdetailbvo")) {
			String billno = (String) vo.getAttributeValue(ApplyDetailBVO.DEF3);
//			if(StringUtil.isBlank(billno)){
//				throw new LfwRuntimeException("【当请款方式为非合同请款时，请款单号不能为空!】");
//			}
			Map<String, Object> purchaseDetail = new HashMap<String, Object>();// 表体数据
			purchaseDetail.put("RequestNo",  billno);//请款单号
			purchaseDetail.put("RequestAmount",  vo.getAttributeValue(ApplyDetailBVO.APPLYMNY));//请款金额
			purchaseDetail.put("ReturnTicketAmount",  vo.getAttributeValue(ApplyDetailBVO.MONEY));//回票金额
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
	 * 板块名称
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
	 * 币种名称
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
	 * 出账公司名称
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
	 * 出账公司编码
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
	 * 预算归属单位名称
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
	 * 预算归属单位编码
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
	 * 预算归属部门名称
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
	 * 预算归属部门编码
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
	 * 报销人单位名称
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
	 * 报销人单位编码
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
	 * 报销人部门名称
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
	 * 获取HR系统deptid
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
	 * 报销人部门编码
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
	 * 报销人名称
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
	 * 报销人用户编码
	 */
	private String getUser_code(String pk_psndoc) {
		String sql = "select s.user_code from sm_user s where s.pk_psndoc='"
				+ pk_psndoc + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * 岗位名称
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
	 * 岗位编码
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
	 * 合同名称
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
	 * 流程类别名称
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
	 * 流程类别编码
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
	 * 供应商名称
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
	 * 申请人名称
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
	 * 申请人用户编码
	 */
	private String getOperator_code(String operator) {
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
	private String getReceiver_name(String receiver) {
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
	private String getTax_rate(String zyx21) {
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
	private String getObjname(Object object) {
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
	private String getObjcode(Object object) {
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
	private String getCosttype_name(Object object) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ object + "'";
		return getIUAPQueryBS(sql);
	}

	/**
	 * 项目名称
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
	 * 业态维名称
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
	 * 车牌部门名称
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
	 * 部门楼层名称
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
	 * 个人银行账号
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
	 * 客户名称
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
	 * 客商账户银行账户
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
	 * 收支项目名称
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
	 * 发票类型名称
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
	 * org_orgs组织名称
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
	 * org_dept部门名称
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
