package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.salebpm.utils.SaleBPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tb.adjbill.AdjustBillAggregatedVO;
import nc.vo.tb.adjbill.AdjustBillBVO;
import nc.vo.tb.adjbill.AdjustBillVO;

import org.apache.commons.lang.StringUtils;
/**
 * 预算调整单
 * @author 吕文杰
 *
 */
public class AdjustBillPushToBpmUtil extends SaleBPMBillUtils{
	static AdjustBillPushToBpmUtil utils;

	public static AdjustBillPushToBpmUtil getUtils() {
		if (utils == null) {
			utils = new AdjustBillPushToBpmUtil();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill) 
			throws Exception{
		// TODO 自动生成的方法存根
		AdjustBillAggregatedVO aggVO = (AdjustBillAggregatedVO) bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		Map<String, Object> formData = getFormData(billCode, aggVO);
		//获取流程名，如果责任主体自带流程名或者为空则取默认流程名（）
		AdjustBillBVO[] vos = (AdjustBillBVO[])aggVO.getChildren(AdjustBillBVO.class);
		String pk_dataent = (String)vos[0].getPk_dataent();
		String processname = QueryDocInfoUtils.getUtils().getProcessname(pk_dataent);
		if(processname==null || "~".equals(processname)){
			processname = ISaleBPMBillCont.getBillNameMap().get(billCode);
		}
		Map<String, String> infoMap = SalePushBPMBillUtils.getUtils()
				.pushBillToBpm(userid, formData,
						processname,
						null, bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def2"));
		aggVO.getParentVO().setAttributeValue("def2", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def3", infoMap.get("ApprovalUrl"));
		return aggVO;
	}
	
	private Map<String, Object> getFormData(String billCode,
			AdjustBillAggregatedVO aggVO) throws BusinessException {
		Map<String, Object> formData = getAdjustBillsInfo(billCode,aggVO);
		return formData;
	}

	private Map<String, Object> getAdjustBillsInfo(String billcode,AdjustBillAggregatedVO aggVO) throws BusinessException {
		// TODO 自动生成的方法存根AdjustBillVO
		Map<String, Object> formData = new HashMap<String, Object>();
		AdjustBillBVO[] childrenVOs = (AdjustBillBVO[]) aggVO.getChildrenVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		
		//表头数据
		// 标题,取表体‘费用预算任务’-预算调整申请
		String name = getTaskdefNameByPk(childrenVOs[0].getPk_task());
		purchase.put("Title",name+"-预算调整申请");
		
		// 申请人、申请部门、申请公司
		Map<String, String> userInfo = getUserInfoByID((String)aggVO.getParentVO().getAttributeValue(AdjustBillVO.CREATEDBY));
		purchase.put("Applicant",userInfo.get("psnname"));
		purchase.put("ApplicantCode",userInfo.get("psncode"));
		purchase.put("ApplicationDepartment",userInfo.get("deptname"));
		purchase.put("ApplicationDepartmentCode",userInfo.get("deptcode"));
		purchase.put("ApplicationCompany",userInfo.get("compname"));
		purchase.put("ApplicationCompanyCode",userInfo.get("compcode"));
		
		//单据类型
		purchase.put("OrderType", billcode);
		
		//申请日期
		purchase.put("ApplicationDate",aggVO.getParentVO().getAttributeValue(AdjustBillVO.CREATEDDATE).toString());
		
		//预算编号
		purchase.put("BudgetNumber",(String)aggVO.getParentVO().getAttributeValue(AdjustBillVO.VBILLNO));
		
		//预算主体
		Map<String, String> orgInfo = QueryDocInfoUtils.getUtils().getOrgInfo(childrenVOs[0].getPk_dataent());
		purchase.put("BudgetSubject",orgInfo.get("name"));
		
		//预算年度、币种
		String dimothers_str = childrenVOs[0].getDimothers_str();
		if(dimothers_str != null){
			String[] arr = dimothers_str.substring(1,dimothers_str.lastIndexOf("]")).split(";");
			for (String string : arr) {
				String[] split = string.split("=");
				if(split[0].trim().equals("CURR")){
					purchase.put("Currency",getCurrNameByPk(split[1].trim()));
				}
				if(split[0].trim().equals("YEAR")){
					purchase.put("BudgetYear",split[1]);
				}
			}
		}else{
			purchase.put("Currency",null);
			purchase.put("BudgetYear",null);
		}
		
		//管理费用预算审批类型
		purchase.put("ManagementExpense", null);
		
		//本次调整比例：【调整金额合计/年初预算合计】
		UFDouble sum1 = new UFDouble(0);
		//UFDouble sum2 = new UFDouble(0);
		for (AdjustBillBVO vo : childrenVOs) {
			sum1 = sum1.add(vo.getAdjustdata()==null?UFDouble.ZERO_DBL:vo.getAdjustdata());
			//sum2 = sum2.add(vo.getDef1()==null?UFDouble.ZERO_DBL:new UFDouble(vo.getDef1()));
		}
		//调整金额合计
		purchase.put("TotalAmount", sum1.setScale(2, UFDouble.ROUND_HALF_UP));
		//年初预算
		//purchase.put("BeginningBudget",sum2.setScale(2, UFDouble.ROUND_HALF_UP));
		//本次调整比例：【调整金额合计/年初预算合计】
		//purchase.put("AdjustmentProportion",sum2.doubleValue()==0?null:(sum1.div(sum2).multiply(100)).setScale(2, UFDouble.ROUND_HALF_UP).toString()+"%");
		
		//调整原因
		purchase.put("AdjustmentReason",aggVO.getParentVO().getAttributeValue(AdjustBillVO.CREATEDNOTE));
		
		// 附件
		String File = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
		if (fileVOs != null && fileVOs.size() > 0) {
			for (int i = 0; i < fileVOs.size(); i++) {
				File += fileVOs.get(i).getDocument_name() + "&"
						+ fileVOs.get(i).getFile_id() + ";";
			}
			purchase.put("File",File.substring(0, File.lastIndexOf(";")));
		} else {
			purchase.put("File", null);
		}
		formData.put("I_ManagementBudget", purchase);//存放表头
		
		//表体数据
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();
		for (AdjustBillBVO vo : childrenVOs) {
			Map<String, Object> bodyPurchase = new HashMap<String, Object>();
			//调整意见
			bodyPurchase.put("AdjustmentOpinions", (String)vo.getAdjustnote());
			//预算调整科目
			bodyPurchase.put("BudgetAdjustment", getBudgetSub("",vo.getPk_measure(),(String)aggVO.getParentVO().getAttributeValue(AdjustBillVO.DEF5)));
			//业态，人员，部门，车牌部门，部门楼层
			String dimothers = vo.getDimothers_str();
			if(dimothers != null){
				String[] dimothersArr = dimothers.substring(1,dimothers.lastIndexOf("]")).split(";");
				for (String string : dimothersArr) {
					String[] split = string.split("=");
					if(split[0].trim().equals("ys004")){//业态
						if(split.length == 1){
							bodyPurchase.put("Format", null);
						}else{
							bodyPurchase.put("Format", getDocNameByPk(split[1]));
						}
					}else if(split[0].trim().equals("DEPT")){//部门
						if(split.length == 1){
							bodyPurchase.put("Department", null);
						}else{
							bodyPurchase.put("Department", getDeptNameByPk(split[1]));
						}
					}else if(split[0].trim().equals("EMPLOYEE")){//人员
						if(split.length == 1){
							bodyPurchase.put("Personnel", null);
						}else{
							bodyPurchase.put("Personnel", getUserNameByPk(split[1]));
						}
					}else if(split[0].trim().equals("ys008")){//车牌部门
						if(split.length == 1){
							bodyPurchase.put("LicensePlate", null);
						}else{
							bodyPurchase.put("LicensePlate", getDocNameByPk(split[1]));
						}
					}
				}
			}else{
				bodyPurchase.put("Format", null);
				bodyPurchase.put("Department", null);
				bodyPurchase.put("Personnel", null);
				bodyPurchase.put("LicensePlate", null);
			}
			//部门楼层
			//bodyPurchase.put("DepartmentFloor", null);
			//本次调整前金额
			bodyPurchase.put("AmountBefore", vo.getData_before());
			//调整金额
			bodyPurchase.put("AdjustmentAmount", vo.getAdjustdata());
			//本次调整后金额
			bodyPurchase.put("AmountAfter", vo.getData_after());
			listPurchase.add(bodyPurchase);
		}
		for (int i = 0; i < listPurchase.size()-1; i++) {
			int monthNum = 1;//合并的数据条数
			if(!listPurchase.get(i).isEmpty()){
				for (int j = i+1; j < listPurchase.size(); j++) {
					//根据人员和预算科目是否相同判断数据是否合并
					String personneli = (String)listPurchase.get(i).get("Personnel");
					personneli = personneli==null?"":personneli;
					String personnelj = (String)listPurchase.get(j).get("Personnel");
					personnelj = personnelj==null?"":personnelj;
					
					String budgetAdjustmenti = (String)listPurchase.get(i).get("BudgetAdjustment");
					budgetAdjustmenti = budgetAdjustmenti==null?"":budgetAdjustmenti;
					String budgetAdjustmentj = (String)listPurchase.get(j).get("BudgetAdjustment");
					budgetAdjustmentj = budgetAdjustmentj==null?"":budgetAdjustmentj;
					
					if(personneli.equals(personnelj) && budgetAdjustmenti.equals(budgetAdjustmentj)){
						monthNum++;
						UFDouble AmountBeforei = (UFDouble)listPurchase.get(i).get("AmountBefore");
						UFDouble AmountBeforej = (UFDouble)listPurchase.get(j).get("AmountBefore");
						AmountBeforei = AmountBeforei==null?UFDouble.ZERO_DBL:AmountBeforei;
						AmountBeforej = AmountBeforej==null?UFDouble.ZERO_DBL:AmountBeforej;
						listPurchase.get(i).put("AmountBefore", (AmountBeforei.add(AmountBeforej)).setScale(2, UFDouble.ROUND_HALF_UP));

						UFDouble AdjustmentAmounti = (UFDouble)listPurchase.get(i).get("AdjustmentAmount");
						UFDouble AdjustmentAmountj = (UFDouble)listPurchase.get(j).get("AdjustmentAmount");
						AdjustmentAmounti = AdjustmentAmounti==null?UFDouble.ZERO_DBL:AdjustmentAmounti;
						AdjustmentAmountj = AdjustmentAmountj==null?UFDouble.ZERO_DBL:AdjustmentAmountj;
						listPurchase.get(i).put("AdjustmentAmount", AdjustmentAmounti.add(AdjustmentAmountj).setScale(2, UFDouble.ROUND_HALF_UP));
						
						UFDouble AmountAfteri = (UFDouble)listPurchase.get(i).get("AmountAfter");
						UFDouble AmountAfterj = (UFDouble)listPurchase.get(j).get("AmountAfter");
						AmountAfteri = AmountAfteri==null?UFDouble.ZERO_DBL:AmountAfteri;
						AmountAfterj = AmountAfterj==null?UFDouble.ZERO_DBL:AmountAfterj;
						listPurchase.get(i).put("AmountAfter", AmountAfteri.add(AmountAfterj).setScale(2, UFDouble.ROUND_HALF_UP));
						
						String stri = (String)listPurchase.get(i).get("AdjustmentOpinions");
						String strj = (String)listPurchase.get(j).get("AdjustmentOpinions");
						if(StringUtils.isNotBlank(stri) && StringUtils.isNotBlank(strj)){
							listPurchase.get(i).put("AdjustmentOpinions",stri+"，"+strj);
						}else{
							if(StringUtils.isNotBlank(stri)){
								listPurchase.get(i).put("AdjustmentOpinions",stri);
							}else{
								listPurchase.get(i).put("AdjustmentOpinions",strj);
							}
						}
						
						listPurchase.get(j).clear();
					}
				}
				listPurchase.get(i).put("Months", monthNum);
			}
			
		}
		for (Iterator<Map<String, Object>> iterator = listPurchase.iterator(); iterator.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			if(map.isEmpty()){
				iterator.remove();
			}
		}
		formData.put("C_ManagementBudget_Detail", listPurchase);
		return formData;
	}
	
}
