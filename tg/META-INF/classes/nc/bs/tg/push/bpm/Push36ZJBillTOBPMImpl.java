package nc.bs.tg.push.bpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.os.outside.TGCallUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.tg.outside.IPush36ZJBillTOBPM;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.sf.allocateapply.AggAllocateApplyVO;
import nc.vo.sf.allocateapply.AllocateApplyBVO;
import nc.vo.sf.allocateapply.AllocateApplyVO;
import nc.vo.sf.fundtransferapply.AggFundTransferApplyVO;
import nc.vo.sf.fundtransferapply.FundTransferApplyBVO;
import nc.vo.sf.fundtransferapply.FundTransferApplyVO;
import nc.vo.tg.outside.ResultVO;

public class Push36ZJBillTOBPMImpl implements IPush36ZJBillTOBPM {


	@Override
	public String onTrans36K1DataTOBPM(AggAllocateApplyVO applyvo) throws BusinessException {//资金下拨
		AllocateApplyVO hvo=(AllocateApplyVO) applyvo.getParentVO();
		Map<String,String> userInfo=DocInfoQryUtils.getUtils().getUserInfoByiD(hvo.getBillmaker());
		Map<String,String> orgInfo=DocInfoQryUtils.getUtils().getOrgInfoByID(hvo.getPk_org());
		Map<String,String> deptInfo=DocInfoQryUtils.getUtils().getdeptBypk(hvo.getVuserdef4());
		Map<String,String> org_tInfo=DocInfoQryUtils.getUtils().getOrgInfoByID(hvo.getPk_payorg());
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		purchase.put("Applicant", userInfo!=null&&userInfo.size()>0?userInfo.get("user_name"):null);//申请人名称
		purchase.put("ApplicantCode", userInfo!=null&&userInfo.size()>0?userInfo.get("user_code"):null);//申请人编码
		purchase.put("ApplicationDate",hvo.getApplydate()!=null? hvo.getApplydate().toStdString():null);//申请日期
		purchase.put("ApplicationCompany", orgInfo!=null&&orgInfo.size()>0?orgInfo.get("name"):null);//申请单位名称
		purchase.put("ApplicationCompanyCode", orgInfo!=null&&orgInfo.size()>0?orgInfo.get("code"):null);//申请单位编码
		purchase.put("ApplicationDepartment", deptInfo!=null&&deptInfo.size()>0?deptInfo.get("name"):null);//申请部门名称
		purchase.put("ApplicationDepartmentCode", deptInfo!=null&&deptInfo.size()>0?deptInfo.get("code"):null);//申请部门编码
		purchase.put("pk_allocateapply_h",hvo.getPk_allocateapply_h());
		purchase.put("vbillno", hvo.getVbillno());
		purchase.put("pk_billtypeid", hvo.getPk_billtype());
		purchase.put("pk_payorg", org_tInfo!=null&&org_tInfo.size()>0?org_tInfo.get("name"):null);
		purchase.put("applytotal", hvo.getApplytotal().toBigDecimal());
		purchase.put("stroke", hvo.getStroke());
		purchase.put("vuserdef1", hvo.getVuserdef1());
		purchase.put("Title", hvo.getMemo());
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		
		for ( CircularlyAccessibleValueObject  bodyVO : applyvo.getChildrenVO()) {
			Map<String, Object> purchasedetail = new HashMap<String, Object>();// 表体数据
			AllocateApplyBVO bvo=(AllocateApplyBVO) bodyVO;

			Map<String,String> recBankInfo=DocInfoQryUtils.getUtils().getBankDocInfo(bvo.getBankname_r());
			Map<String,String> fundPlanInfo=DocInfoQryUtils.getUtils().getfunPlanInfo(bvo.getPk_planitem_r());
			
			purchasedetail.put("remark", bvo.getRemark());//摘要
			purchasedetail.put("bankacccode_r", bvo.getBankacccode_r());//收款银行账户
			purchasedetail.put("bankname_r", recBankInfo!=null&&recBankInfo.size()>0?recBankInfo.get("name"):null);//收款单位开户银行
			purchasedetail.put("bankaccname_r", bvo.getBankaccname_r());
			purchasedetail.put("applyamount", bvo.getApplyamount().toBigDecimal());
			purchasedetail.put("pk_planitem_r", fundPlanInfo!=null&&fundPlanInfo.size()>0?fundPlanInfo.get("name"):null);
			purchasedetail.put("applyallocatedate",bvo.getApplyallocatedate()!=null? bvo.getApplyallocatedate().toStdString():null);
			purchaseDetaillist.add(purchasedetail);
		}
		
		Map<String, Object> formData = new HashMap<String, Object>();// 主子表数据
		formData.put("I_FundDispatchApplicationLL", purchase);
		formData.put("C_TableDetails", purchaseDetaillist);
		Map<String, Object> postdata = new HashMap<String, Object>();// 表头数据
		postdata.put("ProcessName", "邻里-资金下拨申请单");
		postdata.put("Action", "提交");
		postdata.put("Comment", "签核意见");
		postdata.put("Draft", "false");
		String bpmid=hvo.getVuserdef2();
		if(bpmid!=null){
			postdata.put("ExistTaskID", bpmid);
		}
		postdata.put("FormData", formData);
		postdata.put("account", userInfo!=null&&userInfo.size()>0?userInfo.get("user_code"):null);
		postdata.put("pk_field", hvo.getPKFieldName());
		postdata.put("table", hvo.getTableName());
		postdata.put("pk", hvo.getPk_allocateapply_h());
		ResultVO vo=TGCallUtils.getUtils().onDesCallService(hvo.getPrimaryKey(), "BPM", "push36K5toBPM", postdata);
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public String onTrans36K5DataTOBPM(AggFundTransferApplyVO trsnapplyvo) throws BusinessException {//资金调拨
		// TODO 自动生成的方法存根
		
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		FundTransferApplyVO hvo=(FundTransferApplyVO) trsnapplyvo.getParentVO();
		Map<String,String> userInfo=DocInfoQryUtils.getUtils().getUserInfoByiD(hvo.getBillmaker());
		Map<String,String> orgInfo=DocInfoQryUtils.getUtils().getOrgInfoByID(hvo.getPk_org());
//		Map<String,String> deptInfo=DocInfoQryUtils.getUtils().getDeptInfo(orgid, code)(hvo.getPk_org());
		Map<String,String> org_tInfo=DocInfoQryUtils.getUtils().getOrgInfoByID(hvo.getPk_org_t());
		Map<String,String> deptInfo=DocInfoQryUtils.getUtils().getdeptBypk(hvo.getVuserdef4());

		purchase.put("Applicant", userInfo!=null&&userInfo.size()>0?userInfo.get("user_name"):null);//申请人名称
		purchase.put("ApplicantCode", userInfo!=null&&userInfo.size()>0?userInfo.get("user_code"):null);//申请人编码
		purchase.put("ApplicationDate",hvo.getApplydate()!=null? hvo.getApplydate().toStdString():null);//申请日期
		purchase.put("ApplicationCompany", orgInfo!=null&&orgInfo.size()>0?orgInfo.get("name"):null);//申请单位名称
		purchase.put("ApplicationCompanyCode", orgInfo!=null&&orgInfo.size()>0?orgInfo.get("code"):null);//申请单位编码
		purchase.put("ApplicationDepartment", deptInfo!=null&&deptInfo.size()>0?deptInfo.get("name"):null);//申请部门名称
		purchase.put("ApplicationDepartmentCode", deptInfo!=null&&deptInfo.size()>0?deptInfo.get("code"):null);//申请部门编码
		purchase.put("pk_allocateapply_h", hvo.getPk_fundtransferapply_h());//主表主键
		purchase.put("vbillno", hvo.getVbillno());//单据编号
		purchase.put("pk_billtypeid", hvo.getPk_billtype());//单据类型
		purchase.put("pk_org_t", org_tInfo!=null&&org_tInfo.size()>0?org_tInfo.get("name"):null);//调拨组织
		purchase.put("totalamount", hvo.getTotalamount().toBigDecimal());//申请总金额
		purchase.put("stroke", hvo.getStroke());//总比数
		purchase.put("vuserdef1", hvo.getVuserdef1());//用款说明
		purchase.put("Title", hvo.getMemo());
		List<Map<String, Object>> purchaseDetaillist = new ArrayList<>();// 表体数据list
		
		for ( CircularlyAccessibleValueObject  bodyVO : trsnapplyvo.getChildrenVO()) {
			Map<String, Object> purchasedetail = new HashMap<String, Object>();// 表体数据

			FundTransferApplyBVO bvo=(FundTransferApplyBVO) bodyVO;
			
			Map<String,String> payBankInfo=DocInfoQryUtils.getUtils().getBankDocInfo(bvo.getBankname_p());
			Map<String,String> payBankSubInfo=DocInfoQryUtils.getUtils().getBankSubDocInfo(bvo.getPk_bankacc_p());
			Map<String,String> recBankInfo=DocInfoQryUtils.getUtils().getBankDocInfo(bvo.getBankname_r());
			Map<String,String> org_rInfo=DocInfoQryUtils.getUtils().getOrgInfoByID(bvo.getPk_org_r());
			Map<String,String> fundPlanInfo=DocInfoQryUtils.getUtils().getfunPlanInfo(bvo.getPk_planitem_p());
			purchasedetail.put("remark", bvo.getRemark());//摘要
			purchasedetail.put("pk_bankacc_p", payBankSubInfo!=null&&payBankSubInfo.size()>0?payBankSubInfo.get("accnum"):null);//付款银行账户
			purchasedetail.put("bankname_p", payBankInfo!=null&&payBankInfo.size()>0?payBankInfo.get("name"):null);//付款银行
			purchasedetail.put("amount", bvo.getAmount().toBigDecimal());//申请金额
			purchasedetail.put("pk_org_r", org_rInfo!=null&&org_rInfo.size()>0?org_rInfo.get("name"):null);//收款单位
			purchasedetail.put("bankacccode_r", bvo.getBankacccode_r());//收款银行账户
			purchasedetail.put("bankname_r", recBankInfo!=null&&recBankInfo.size()>0?recBankInfo.get("name"):null);//收款单位开户银行
			purchasedetail.put("bankaccname_r", bvo.getBankaccname_r());//收款银行账户户名
			purchasedetail.put("pk_planitem_p",  fundPlanInfo!=null&&fundPlanInfo.size()>0?fundPlanInfo.get("name"):null);//调拨计划项目
			purchaseDetaillist.add(purchasedetail);
		}
		
		Map<String, Object> formData = new HashMap<String, Object>();// 主子表数据
		formData.put("I_FundAllocationApplicationLL", purchase);
		formData.put("C_BillDetail", purchaseDetaillist);
		Map<String, Object> postdata = new HashMap<String, Object>();// 表头数据
		String bpmid=hvo.getVuserdef2();
		if(bpmid!=null){
			postdata.put("ExistTaskID", bpmid);
		}
		postdata.put("ProcessName", "邻里-资金调拨申请单");
		postdata.put("Action", "提交");
		postdata.put("Comment", "签核意见");
		postdata.put("Draft", "false");
		postdata.put("FormData", formData);
		postdata.put("account", userInfo!=null&&userInfo.size()>0?userInfo.get("user_code"):null);
		postdata.put("pk_field", hvo.getPKFieldName());
		postdata.put("table", hvo.getTableName());
		postdata.put("pk", hvo.getPk_fundtransferapply_h());
		ResultVO vo=TGCallUtils.getUtils().onDesCallService(trsnapplyvo.getParentVO().getPrimaryKey(), "BPM", "push36K5toBPM", postdata);
//		postdata.put("DeptID", deptid == null ? "" : deptid);
		// TODO 自动生成的方法存根
		return null;
	}
	
	
	

}
