package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.LeaseconBVO;
import nc.vo.fct.ap.entity.PMPlanBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapBillUtils extends EBSBillUtils {
	static FctapBillUtils utils;

	public static FctapBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapBillUtils();
		}
		return utils;
	}

	/**
	 * 付款合同保存
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		//表头数据
		JSONObject headJSON = (JSONObject)value.get("headInfo");
		
		String vbillcode = headJSON.getString("vbillcode");// 单据编号(ebs合同编号)
		String pk = headJSON.getString("pk_fct_ap");// 单据主键
		
		//目标单据名+请求单据号作队列
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		//目标单据名+请求单据pk作日志输出
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;
		
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		AggCtApVO aggVO = null;
		//返回报文
		HashMap<String, String> dataMap = null;
		try {
			//检查NC是否有相应的单据存在
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and pk_fct_ap = '" + pk + "'");
			//数据转换工具对象
			FctapConvertor fctapConvertor = new FctapConvertor();
			//配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "付款合同");
			fctapConvertor.setHVOKeyName(hVOKeyName);
			
			//配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "付款计划页签（签约金额）");
			bVOKeyName.put("supplyAgreementBVO", "补充协议");
			bVOKeyName.put("fct_ap_b", "成本拆分页签（合同基本）");
			bVOKeyName.put("fct_pmplan", "付款计划_保证金_押金_诚意金_共管资金");
			bVOKeyName.put("leaseconBVO", "租赁协议");
			bVOKeyName.put("fct_execution", "执行情况");
			fctapConvertor.setBVOKeyName(bVOKeyName);
			
			// 配置表头检验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "财务组织(ebs出账公司)");
			hKeyName.put("subscribedate", "签字盖章日期");
			hKeyName.put("cvendorid", "供应商");
			hKeyName.put("plate", "板块");
			hKeyName.put("subbudget", "预算主体");
			hKeyName.put("contype", "EBS合同类型");
			hKeyName.put("condetails", "合同细类");
			hKeyName.put("vbillcode", "合同编码");
			hKeyName.put("ctname", "合同名称");
			hKeyName.put("proname", "项目名称");
			hKeyName.put("pronode", "项目节点");
			hKeyName.put("b_lease", "是否租赁合同");
			hKeyName.put("b_payed", "是否存在已付款项");
			hKeyName.put("b_stryear", "是否预算跨年合同");
			hKeyName.put("accountorg", "出账公司");
			hKeyName.put("first", "甲方");
			hKeyName.put("second", "乙方");
			hKeyName.put("third", "丙方");
			hKeyName.put("fourth", "丁方");
			hKeyName.put("fifth", "戊方");
			hKeyName.put("sixth", "己方");
			hKeyName.put("vdef19", "合同属性(ebs单据类型)");
			hKeyName.put("d_sign", "签约日期");
			hKeyName.put("d_creator", "ebs创建日期");
			hKeyName.put("personnelid", "承办人员");
			hKeyName.put("conadmin", "合同管理人");
			hKeyName.put("grade", "经办职位");
			hKeyName.put("con_abstract", "合同摘要");
			hKeyName.put("vdef17", "押金金额");
			hKeyName.put("m_sign", "签约金额");
			hKeyName.put("rate", "税率");
			hKeyName.put("con_link", "合同文本链接");
			hKeyName.put("depid", "承办部门");
			hKeyName.put("vdef9", "补充协议金额");
			hKeyName.put("vdef11", "动态金额");
			hKeyName.put("fstatusflag", "合同状态");
			
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);
			
			// 配置表体检验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			//**********执行情况页签字段检验
			Map<String, String> bExecutionBVOKeyName = new HashMap<String, String>();
			bExecutionBVOKeyName.put("billno", "付款申请号");
			bExecutionBVOKeyName.put("d_apply", "申请日期");
			bExecutionBVOKeyName.put("m_apply", "申请金额");
			bExecutionBVOKeyName.put("m_pay", "本次支付金额");
			bExecutionBVOKeyName.put("m_return", "退回金额");
			bExecutionBVOKeyName.put("d_return", "退回日期");
			bExecutionBVOKeyName.put("m_unpaid", "合同未付金额");
			bExecutionBVOKeyName.put("m_apply_unpaid", "已请款未付");
			bExecutionBVOKeyName.put("def1", "累计已请款");
			bExecutionBVOKeyName.put("def2", "累计已付款");
			bExecutionBVOKeyName.put("def3", "累计已收发票");
			bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			//**********执行情况页签字段检验
			
			//**********付款计划页签字段检验
			Map<String, String> bCtApPlanVOKeyName = new HashMap<String, String>();
			bCtApPlanVOKeyName.put("d_payplan", "计划付款日期");
			bCtApPlanVOKeyName.put("planrate", "付款比例");
			bCtApPlanVOKeyName.put("planmoney", "付款金额");
			bCtApPlanVOKeyName.put("pay_condition", "付款条件");
			bCtApPlanVOKeyName.put("paymenttype", "款项类型");
			bCtApPlanVOKeyName.put("def8", "操作方式");
			bCtApPlanVOKeyName.put("def9", "后续处理");
			bCtApPlanVOKeyName.put("def10", "借款利率");
			bCtApPlanVOKeyName.put("d_return", "预计退回时间");
			bCtApPlanVOKeyName.put("def4", "实付/对冲日期");
			bCtApPlanVOKeyName.put("def5", "对冲款项性质");
			bCtApPlanVOKeyName.put("def6", "累计请款金额");
			bCtApPlanVOKeyName.put("def7", "累计付款金额");
			bValidatedKeyName.put("ctApPlanVO", bCtApPlanVOKeyName);
			//**********付款计划页签字段检验
			
			//**********付款计划_保证金_押金_诚意金_共管资金页签字段检验
			Map<String, String> bPMPlanBVOKeyName = new HashMap<String, String>();
			bPMPlanBVOKeyName.put("d_paymonery", "计划付款日期");
			bPMPlanBVOKeyName.put("pay_proportion", "付款比例");
			bPMPlanBVOKeyName.put("m_offset", "抵冲金额");
			bPMPlanBVOKeyName.put("m_pay", "付款金额");
			bPMPlanBVOKeyName.put("paycondition", "付款条件");
			bPMPlanBVOKeyName.put("m_payed", "已付金额");
			bPMPlanBVOKeyName.put("paytype", "款项类型");
			bPMPlanBVOKeyName.put("deal", "后续处理");
			bPMPlanBVOKeyName.put("d_return", "预计退回日期");
			bPMPlanBVOKeyName.put("conreturn", "退回条件");
			bPMPlanBVOKeyName.put("b_refund", "是否退款");
			bPMPlanBVOKeyName.put("d_refund", "收到退款日期");
			bPMPlanBVOKeyName.put("operation", "操作方式");
			bValidatedKeyName.put("fct_pmplan", bPMPlanBVOKeyName);
			//**********付款计划_保证金_押金_诚意金_共管资金页签字段检验
			
			//**********补充协议页签字段检验
			Map<String, String> bSupplyAgreementBVOKeyName = new HashMap<String, String>();
			bSupplyAgreementBVOKeyName.put("billno", "协议编号");
			bSupplyAgreementBVOKeyName.put("name", "协议名称");
			bSupplyAgreementBVOKeyName.put("m_monery", "协议金额");
			bSupplyAgreementBVOKeyName.put("d_date", "签约日期");
			bSupplyAgreementBVOKeyName.put("m_supply", "协议金额累计");
			bValidatedKeyName.put("supplyAgreementBVO", bSupplyAgreementBVOKeyName);
			//**********补充协议页签字段检验
			
			//**********租赁合同页签字段检验
			Map<String, String> bLeaseconBVOKeyName = new HashMap<String, String>();
			bLeaseconBVOKeyName.put("lease", "租赁物");
			bLeaseconBVOKeyName.put("area", "租赁面积");
			bLeaseconBVOKeyName.put("d_start", "租赁开始日");
			bLeaseconBVOKeyName.put("d_end", "租赁结束日");
			bLeaseconBVOKeyName.put("b_fixedfunds", "是否固定租金");
			bLeaseconBVOKeyName.put("clauses", "具体条款(浮动租金递增幅度(如%)or具体条款)");
			bValidatedKeyName.put("leaseconBVO", bLeaseconBVOKeyName);
			//**********租赁合同页签字段检验
			
			//**********合同基本（成本拆分）页签字段检验
			Map<String, String> bCtApBVOKeyName = new HashMap<String, String>();
			bCtApBVOKeyName.put("vbdef11", "科目名称");
			bCtApBVOKeyName.put("vbdef13", "拆分比例");
			bCtApBVOKeyName.put("vbdef14", "金额");
			bCtApBVOKeyName.put("vbdef15", "业态");
			bCtApBVOKeyName.put("vbdef16", "车辆部门");
			bCtApBVOKeyName.put("vbdef17", "部门/楼层");
			bCtApBVOKeyName.put("vbdef18", "用款人");
			bCtApBVOKeyName.put("vbdef19", "是否预提");
			bCtApBVOKeyName.put("vbdef20", "说明");
			bCtApBVOKeyName.put("vbdef12", "预算金额");
			bCtApBVOKeyName.put("vbdef30", "预算年度");
			bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			//**********合同基本（成本拆分）页签字段检验
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);
			
			// 配置参照字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("pk_org");
			refKeys.add("cvendorid");
			refKeys.add("personnelid");
			refKeys.add("depid");
			fctapConvertor.setRefKeys(refKeys);
			
			// 设置组织默认信息
			fctapConvertor.setDefaultGroup("000112100000000005FD");
			
			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value, AggCtApVO.class);
			
			// 表头默认信息配置
			billvo.getParentVO().setAttributeValue("ctrantypeid", "FCT1-01"); // 交易类型
			billvo.getParentVO().setAttributeValue("cbilltypecode", "FCT1"); // 单据类型
			billvo.getParentVO().setAttributeValue("valdate", new UFDate()); // 计划生效日期
			billvo.getParentVO().setAttributeValue("invallidate", new UFDate());// 计划终止日期
			billvo.getParentVO().setAttributeValue("nexchangerate", new UFDouble());// 折本汇率
			
			// 表体默认信息配置
			
			ISuperVO[] ctApBVOs =  billvo.getChildren(CtApBVO.class);// 成本拆分页签（合同基本）
			for(ISuperVO ctApBVO : ctApBVOs){
				ctApBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
				ctApBVO.setAttributeValue("pk_org", billvo.getParentVO().getPk_org());
				ctApBVO.setAttributeValue("pk_org_v", billvo.getParentVO().getPk_org());
				ctApBVO.setAttributeValue("pk_group", billvo.getParentVO().getPk_group());
				ctApBVO.setAttributeValue("ftaxtypeflag", 1);
			}
			
			ISuperVO[] leaseconBVOs =  billvo.getChildren(LeaseconBVO.class);// 租赁协议
			for(ISuperVO leaseconBVO : leaseconBVOs){
				leaseconBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] supplyAgreementBVOs =  billvo.getChildren(SupplyAgreementBVO.class);// 补充协议
			for(ISuperVO supplyAgreementBVO : supplyAgreementBVOs){
				supplyAgreementBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] pMPlanBVOs =  billvo.getChildren(PMPlanBVO.class);// 付款计划_保证金_押金_诚意金_共管资金
			for(ISuperVO pMPlanBVO : pMPlanBVOs){
				pMPlanBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			ISuperVO[] ctApPlanVOs =  billvo.getChildren(CtApPlanVO.class);// 付款计划页签（签约金额）
			for(ISuperVO ctApPlanVO : ctApPlanVOs){
				ctApPlanVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
				ctApPlanVO.setAttributeValue("pk_org", billvo.getParentVO().getPk_org());
				ctApPlanVO.setAttributeValue("pk_group", billvo.getParentVO().getPk_group());
			}
			
			ISuperVO[] executionBVOs =  billvo.getChildren(ExecutionBVO.class);// 执行情况
			for(ISuperVO executionBVO : executionBVOs){
				executionBVO.setAttributeValue("pk_fct_ap", billvo.getParentVO().getPk_fct_ap());
			}
			
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			
			AggCtApVO billVO = null;
			if(aggVO == null){
				//不存在，新增单据
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction("SAVEBASE",
						"FCT1", null, billvo, null, eParam))[0];
			}else{
				//存在更新单据
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction("SAVEBASE",
						"FCT1", null, billvo, null, eParam))[0];
			}
			
			dataMap = new HashMap<String, String>();
			dataMap.put("billid", billVO.getPrimaryKey());
			dataMap.put("billno", (String) billVO.getParentVO()
					.getAttributeValue("vbillcode"));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);

	}
}
