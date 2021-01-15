package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.FctMoretax;
import nc.vo.fct.ap.entity.LeaseconBVO;
import nc.vo.fct.ap.entity.PMPlanBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapOutBillUtils extends EBSBillUtils {
	static FctapOutBillUtils utils;

	public static FctapOutBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapOutBillUtils();
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
		// 表头数据
		JSONObject headJSON = (JSONObject) value.get("headInfo");

		String vbillcode = headJSON.getString("vbillcode");// 单据编号(ebs合同编号)
		String pk = headJSON.getString("def49");// ebs表头合同主键

		// 目标单据名+请求单据号作队列
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":"
				+ vbillcode;
		// 目标单据名+请求单据pk作日志输出
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + pk;

		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		AggCtApVO aggVO = null;
		// 返回报文
		Map<String, Object> dataMap = null;
		try {
			// 检查NC是否有相应的单据存在
			aggVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and blatest ='Y'  and pk_fct_ap = '" + pk
							+ "'");
			String hpk = null;
			String def105=null;//包含在成本台账中”（表头def105）
			String def106=null;//单据是否走财务变更按钮修改数据标志（如果为Y，def105取原值）
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
			    def106=aggVO.getParentVO().getDef106();
			    def105=aggVO.getParentVO().getDef105();
			}
			// 数据转换工具对象
			FctapOutConvertor fctapConvertor = new FctapOutConvertor();
			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "付款合同");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "付款计划页签（签约金额）");
			bVOKeyName.put("supplyAgreementBVO", "补充协议");
			bVOKeyName.put("fct_ap_b", "成本拆分页签（合同基本）");
			bVOKeyName.put("fct_pmplan", "付款计划_保证金_押金_诚意金_共管资金");
			bVOKeyName.put("leaseconBVO", "租赁协议");
			bVOKeyName.put("fct_execution", "执行情况");
			bVOKeyName.put("fct_moretax", "多税率");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// 配置表头检验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			hKeyName.put("pk_org", "财务组织(ebs出账公司)");
			// hKeyName.put("subscribedate", "签字盖章日期");
			// hKeyName.put("cvendorid", "供应商");
			// hKeyName.put("plate", "板块");
			hKeyName.put("ebscontractstatus", "ebs合同状态");
			hKeyName.put("subbudget", "预算主体");
			// hKeyName.put("contype", "EBS合同类型");
			// hKeyName.put("condetails", "合同细类");
			hKeyName.put("vbillcode", "合同编码");
			hKeyName.put("ctname", "合同名称");
			// hKeyName.put("proname", "项目名称");
			// hKeyName.put("pronode", "项目节点");
			// hKeyName.put("b_lease", "是否租赁合同");
			// hKeyName.put("b_payed", "是否存在已付款项");
			// hKeyName.put("b_stryear", "是否预算跨年合同");
			hKeyName.put("accountorg", "出账公司");
			// hKeyName.put("first", "甲方");
			// hKeyName.put("second", "乙方");
			// hKeyName.put("third", "丙方");
			// hKeyName.put("fourth", "丁方");
			// hKeyName.put("fifth", "戊方");
			// hKeyName.put("sixth", "己方");
			hKeyName.put("vdef19", "合同属性(ebs单据类型)");
			// hKeyName.put("d_sign", "签约日期");
			hKeyName.put("d_creator", "ebs创建日期");
			// hKeyName.put("personnelid", "经办（承办）人员");
			hKeyName.put("conadmin", "合同管理人");
			// hKeyName.put("grade", "经办（承办）职位");
			// hKeyName.put("con_abstract", "合同摘要");
			// hKeyName.put("vdef17", "押金/保证金/共管资金金额");
			// hKeyName.put("ntotalorigmny", "签约金额");
			// hKeyName.put("rate", "税率");
			// hKeyName.put("con_link", "合同文本链接");
			// hKeyName.put("depid", "经办（承办）部门");
			// hKeyName.put("vdef9", "补充协议金额");
			// hKeyName.put("vdef11", "动态金额");
			// hKeyName.put("fstatusflag", "合同状态");
			// hKeyName.put("vdef20", "累计已收发票/票据");
			hKeyName.put("corigcurrencyid", "币种");
			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// 配置表体检验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********执行情况页签字段检验
			/*
			 * Map<String, String> bExecutionBVOKeyName = new HashMap<String,
			 * String>(); bExecutionBVOKeyName.put("pk_ebs", "ebs主键");
			 * bExecutionBVOKeyName.put("billno", "付款申请号");
			 * bExecutionBVOKeyName.put("d_apply", "申请日期");
			 * bExecutionBVOKeyName.put("mapply", "申请金额");
			 * bExecutionBVOKeyName.put("mpay", "已付金额");
			 * bExecutionBVOKeyName.put("paydate", "付款日期");
			 * bExecutionBVOKeyName.put("minvalid", "作废金额");
			 * bExecutionBVOKeyName.put("d_invalid", "作废日期");
			 * bExecutionBVOKeyName.put("mreturn", "退回金额");
			 * bExecutionBVOKeyName.put("d_return", "退回日期");
			 * bExecutionBVOKeyName.put("munpaid", "合同未付金额");
			 * bExecutionBVOKeyName.put("mapplyunpaid", "已请款未付");
			 * bExecutionBVOKeyName.put("def1", "累计已请款");
			 * bExecutionBVOKeyName.put("def2", "累计已付款"); //
			 * bExecutionBVOKeyName.put("def3", "累计已收发票");
			 * bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			 */
			// **********执行情况页签字段检验

			// **********付款计划页签字段检验
			/*
			 * Map<String, String> bCtApPlanVOKeyName = new HashMap<String,
			 * String>(); bCtApPlanVOKeyName.put("d_payplan", "计划付款日期");
			 * bCtApPlanVOKeyName.put("planrate", "计划付款比例");
			 * bCtApPlanVOKeyName.put("planmoney", "计划付款金额");
			 * bCtApPlanVOKeyName.put("pay_condition", "付款条件");
			 * bCtApPlanVOKeyName.put("paymenttype", "款项类型");
			 * bCtApPlanVOKeyName.put("def8", "操作方式");
			 * bCtApPlanVOKeyName.put("def9", "后续处理");
			 * bCtApPlanVOKeyName.put("def10", "借款利率");
			 * bCtApPlanVOKeyName.put("d_return", "预计退回（归还）时间"); //
			 * bCtApPlanVOKeyName.put("def4", "实付/对冲日期"); //
			 * bCtApPlanVOKeyName.put("def5", "对冲款项性质");
			 * bCtApPlanVOKeyName.put("msumapply", "累计请款金额");
			 * bCtApPlanVOKeyName.put("msumpayed", "累计已付款金额");
			 * bCtApPlanVOKeyName.put("msumallpayed", "全部付款计划的金额合计（合计行）");
			 * bCtApPlanVOKeyName.put("compensateno", "抵冲单号");
			 * bCtApPlanVOKeyName.put("mcompensate", "抵冲金额");
			 * bCtApPlanVOKeyName.put("compensatecor", "抵冲出账公司");
			 * bCtApPlanVOKeyName.put("pk_ebs", "ebs主键");
			 * bValidatedKeyName.put("fct_ap_plan", bCtApPlanVOKeyName);
			 */
			// **********付款计划页签字段检验

			// **********付款计划_保证金_押金_诚意金_共管资金页签字段检验
			/*
			 * Map<String, String> bPMPlanBVOKeyName = new HashMap<String,
			 * String>(); bPMPlanBVOKeyName.put("d_paymonery", "计划付款日期");
			 * bPMPlanBVOKeyName.put("pay_proportion", "付款比例");
			 * bPMPlanBVOKeyName.put("mpay", "计划付款金额");
			 * bPMPlanBVOKeyName.put("paycondition", "付款条件"); //
			 * bPMPlanBVOKeyName.put("m_payed", "已付金额");
			 * bPMPlanBVOKeyName.put("paytype", "款项类型");
			 * bPMPlanBVOKeyName.put("deal", "后续处理");
			 * bPMPlanBVOKeyName.put("d_return", "预计退回/解付日期");
			 * bPMPlanBVOKeyName.put("conreturn", "退回/解付条件"); //
			 * bPMPlanBVOKeyName.put("b_refund", "是否退款");
			 * bPMPlanBVOKeyName.put("d_refund", "收到退款日期");
			 * bPMPlanBVOKeyName.put("operation", "操作方式");
			 * bPMPlanBVOKeyName.put("mback", "退款金额");
			 * bPMPlanBVOKeyName.put("offsetno", "抵冲单号");
			 * bPMPlanBVOKeyName.put("moffset", "抵冲金额");
			 * bPMPlanBVOKeyName.put("offsetcompany", "抵冲出账公司");
			 * bPMPlanBVOKeyName.put("msumapply", "累计请款金额");
			 * bPMPlanBVOKeyName.put("msumpay", "累计已付款金额");
			 * bPMPlanBVOKeyName.put("msumallpay", "全部付款计划的金额合计(合计行)");
			 * bPMPlanBVOKeyName.put("pk_ebs", "ebs主键");
			 * bValidatedKeyName.put("fct_pmplan", bPMPlanBVOKeyName);
			 */
			// **********付款计划_保证金_押金_诚意金_共管资金页签字段检验

			// **********补充协议页签字段检验
			/*
			 * Map<String, String> bSupplyAgreementBVOKeyName = new
			 * HashMap<String, String>();
			 * bSupplyAgreementBVOKeyName.put("pk_ebs", "ebs主键");
			 * bSupplyAgreementBVOKeyName.put("billno", "协议编号");
			 * bSupplyAgreementBVOKeyName.put("name", "协议名称");
			 * bSupplyAgreementBVOKeyName.put("mmonery", "协议金额");
			 * bSupplyAgreementBVOKeyName.put("d_date", "签约日期");
			 * bSupplyAgreementBVOKeyName.put("msupply", "协议金额累计");
			 * bValidatedKeyName.put("supplyAgreementBVO",
			 * bSupplyAgreementBVOKeyName);
			 */
			// **********补充协议页签字段检验

			// **********租赁合同页签字段检验
			/*
			 * Map<String, String> bLeaseconBVOKeyName = new HashMap<String,
			 * String>(); bLeaseconBVOKeyName.put("pk_ebs", "ebs主键");
			 * bLeaseconBVOKeyName.put("lease", "租赁物");
			 * bLeaseconBVOKeyName.put("area", "租赁面积");
			 * bLeaseconBVOKeyName.put("d_start", "租赁开始日");
			 * bLeaseconBVOKeyName.put("d_end", "租赁结束日");
			 * bLeaseconBVOKeyName.put("b_fixedfunds", "是否固定租金");
			 * bLeaseconBVOKeyName.put("clauses", "具体条款(浮动租金递增幅度(如%)or具体条款)");
			 * bValidatedKeyName.put("leaseconBVO", bLeaseconBVOKeyName);
			 */
			// **********租赁合同页签字段检验

			// **********多税率页签字段检验
			Map<String, String> bFctMoretaxKeyName = new HashMap<String, String>();
			bFctMoretaxKeyName.put("pk_ebs", "ebs主键");
			bFctMoretaxKeyName.put("maininvoicetype", "主要发票类型");
			// bFctMoretaxKeyName.put("taxrate", "税率");
			bFctMoretaxKeyName.put("intaxmny", "含税金额");
			// bFctMoretaxKeyName.put("taxmny", "税额");
			// bFctMoretaxKeyName.put("notaxmny", "不含税金额");
			bValidatedKeyName.put("fct_moretax", bFctMoretaxKeyName);
			// **********多税率页签字段检验

			// **********合同基本（成本拆分）页签字段检验
			/*
			 * Map<String, String> bCtApBVOKeyName = new HashMap<String,
			 * String>(); bCtApBVOKeyName.put("pk_ebs", "ebs主键");
			 * bCtApBVOKeyName.put("vbdef11", "科目名称");
			 * bCtApBVOKeyName.put("vbdef13", "拆分比例");
			 * bCtApBVOKeyName.put("vbdef14", "金额"); //
			 * bCtApBVOKeyName.put("norigtaxmny", "金额");
			 * bCtApBVOKeyName.put("vbdef15", "业态");
			 * bCtApBVOKeyName.put("vbdef16", "车辆部门");
			 * bCtApBVOKeyName.put("vbdef17", "部门/楼层");
			 * bCtApBVOKeyName.put("vbdef18", "用款人");
			 * bCtApBVOKeyName.put("vbdef19", "是否预提");
			 * bCtApBVOKeyName.put("vbdef20", "说明"); //
			 * bCtApBVOKeyName.put("vbdef12", "预算金额");
			 * bCtApBVOKeyName.put("vbdef30", "预算年度");
			 * bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			 */
			// **********合同基本（成本拆分）页签字段检验
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// 配置参照字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-pk_org");
			// refKeys.add("fct_ap-grade"); // 经办（承办）职位 参照（岗位）*
			refKeys.add("fct_ap-first"); // 甲方（财务组织）*
			refKeys.add("fct_ap-second");// 乙方（供应商）*
			refKeys.add("fct_ap-third");// 丙方（供应商）*
			refKeys.add("fct_ap-fourth");// 丁方（供应商）*
			refKeys.add("fct_ap-fifth");// 戊方（供应商）*
			refKeys.add("fct_ap-sixth");// 己方（供应商）*
			refKeys.add("fct_ap-corigcurrencyid");// 币种
			refKeys.add("fct_ap-accountorg");// 出账公司(财务组织)*
			refKeys.add("fct_ap-subbudget");// 预算主体(参照财务组织)*
			refKeys.add("fct_ap_plan-compensatecor");// 抵冲出账公司（付款计划签约金额） 参照财务组织*
			refKeys.add("fct_pmplan-offsetcompany");// 抵冲出账公司(付款计划押金) 参照财务组织*
			// refKeys.add("fct_ap-cvendorid"); // 供应商
			// refKeys.add("fct_ap-personnelid");// 经办（承办）人*
			refKeys.add("fct_ap-conadmin");// 合同管理人*
			// refKeys.add("fct_ap-depid");// 经办（承办）部门*
			refKeys.add("fct_ap-plate");// 板块*
			// refKeys.add("fct_ap-proname");// 项目名称*
			refKeys.add("fct_moretax-maininvoicetype");// 主要发票类型*
			refKeys.add("fct_pmplan-paytype");// 款项类型-付款计划（保证金、押金、诚意金和共管资金）*
			refKeys.add("fct_ap_plan-def2");// 款项类型-付款计划（签约金额）*
			refKeys.add("fct_ap_b-vbdef11");// 科目名称（预算科目）-合同基本（成本拆分）*
			refKeys.add("fct_ap_b-vbdef15");// 业态-合同基本（成本拆分）*
			refKeys.add("fct_ap_b-vbdef16");// 车辆部门-合同基本（成本拆分）*
			refKeys.add("fct_ap_b-vbdef17");// 部门/楼层-合同基本（成本拆分）*
			refKeys.add("fct_ap_b-vbdef18");// 用款人-合同基本（成本拆分）*
			fctapConvertor.setRefKeys(refKeys);

			// 设置组织默认信息
			fctapConvertor.setDefaultGroup("000112100000000005FD");

			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// ********************表头参照信息设置***************
			billvo.getParentVO().setPk_org(
					fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
							.getParentVO().getPk_org())); // 财务组织
			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getPk_org()));
			billvo.getParentVO().setAccountorg(
					fctapConvertor.getRefAttributePk("fct_ap-accountorg",
							billvo.getParentVO().getAccountorg())); // 出账公司(财务组织)
			billvo.getParentVO().setSubbudget(
					fctapConvertor.getRefAttributePk("fct_ap-subbudget", billvo
							.getParentVO().getSubbudget())); // 预算主体(参照财务组织)
			billvo.getParentVO().setPlate(
					fctapConvertor.getRefAttributePk("fct_ap-plate", billvo
							.getParentVO().getPlate())); // 板块
			billvo.getParentVO().setProname(billvo.getParentVO().getProname());// 项目名称（项目）

			billvo.getParentVO().setFirst(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFirst())); // 甲方（财务组织）

			billvo.getParentVO().setSecond(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getSecond()));// 乙方（供应商）
			billvo.getParentVO().setThird(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getThird()));// 丙方（供应商）
			billvo.getParentVO().setFourth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFourth()));// 丁方（供应商）
			billvo.getParentVO().setFifth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getFifth()));// 戊方（供应商）
			billvo.getParentVO().setSixth(
					fctapConvertor.getRefAttributePk("fct_ap-supplier", billvo
							.getParentVO().getSixth()));// 己方（供应商）
			// 币种
			billvo.getParentVO().setCorigcurrencyid(
					fctapConvertor.getRefAttributePk("fct_ap-corigcurrencyid",
							billvo.getParentVO().getCorigcurrencyid()));// 币种

			// Map<String, String> psnInfoMap = getPsnInfo(billvo.getParentVO()
			// .getPersonnelid(), billvo.getParentVO().getDepid(), billvo
			// .getParentVO().getGrade());
			// if (psnInfoMap == null) {
			// throw new BusinessException("经办人["
			// + billvo.getParentVO().getPersonnelid() + "],经办部门["
			// + billvo.getParentVO().getDepid() + "],经办职位["
			// + billvo.getParentVO().getGrade()
			// + "]未能在NC人员工作档案上查询到相关信息");
			// }

			// billvo.getParentVO().setPersonnelid(psnInfoMap.get("pk_psndoc"));
			// // 经办（承办）人
			// billvo.getParentVO().setDepid(psnInfoMap.get("pk_dept")); //
			// 经办（承办）部门
			// billvo.getParentVO().setGrade(psnInfoMap.get("pk_post")); // 经办职位
			billvo.getParentVO().setDef58(billvo.getParentVO().getDef58()); // 经办（承办）人
			billvo.getParentVO().setDef59(billvo.getParentVO().getDef59()); // 经办（承办）部门
			billvo.getParentVO().setDef60(billvo.getParentVO().getDef60()); // 经办职位
			String def76 = billvo.getParentVO().getDef76();
			if (def76 != null && !"".equals(def76)) {
				billvo.getParentVO().setDef76(def76);// 是否自动扣款（必填）
				billvo.getParentVO().setDef77(billvo.getParentVO().getDef77());// 签约账户
				billvo.getParentVO().setDef78(billvo.getParentVO().getDef78());// 划扣类型
			} else {
				throw new BusinessException("是否自动扣款不可为空!");
			}
			billvo.getParentVO().setDef79(billvo.getParentVO().getDef79());// ebs版本主键
			// billvo.getParentVO().setConadmin(
			// fctapConvertor.getRefAttributePk("fct_ap-conadmin", billvo
			// .getParentVO().getConadmin(), billvo.getParentVO()
			// .getDepid())); // 合同管理人
			// ********************表头参照信息设置***************

			// 表头默认信息配置
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-003")); // 交易类型
			billvo.getParentVO().setCbilltypecode("FCT1"); // 单据类型
			billvo.getParentVO().setValdate(new UFDate()); // 计划生效日期
			billvo.getParentVO().setInvallidate(new UFDate());// 计划终止日期
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// 折本汇率
			billvo.getParentVO().setSubscribedate(new UFDate());// 盖章签字日期
			billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());// 供应商（取乙方）
			UFDouble ntotalorigmny = (UFDouble) billvo.getParent()
					.getAttributeValue("ntotalorigmny");
			billvo.getParentVO().setMsign(ntotalorigmny);// 签约金额
			// 表体默认信息配置

			ISuperVO[] ctApBVOs = billvo.getChildren(CtApBVO.class);// 成本拆分页签（合同基本）
			int ctApBVORowNo = 10;
			if (ctApBVOs != null && ctApBVOs.length > 0) {
				for (ISuperVO tmpCtApBVO : ctApBVOs) {
					CtApBVO ctApBVO = (CtApBVO) tmpCtApBVO;
					ctApBVO.setCrowno(String.valueOf(ctApBVORowNo));
					ctApBVO.setPk_fct_ap(hpk);
					ctApBVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApBVO.setPk_org_v(billvo.getParentVO().getPk_org());
					ctApBVO.setPk_group(billvo.getParentVO().getPk_group());
					ctApBVO.setFtaxtypeflag(1); // 扣税类别
					ctApBVO.setNorigtaxmny(billvo.getParentVO()
							.getNtotalorigmny()); // 原币价税合计
					ctApBVO.setAttributeValue("ntaxmny", new UFDouble(1)); // 本币价税合计
					ctApBVORowNo += 10;
               
					// **********成本拆分（合同基本）参照设值
					ctApBVO.setVbdef11(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef11", ctApBVO.getVbdef11(),
							ctApBVO.getPk_org())); // 科目名称（预算科目）
					ctApBVO.setVbdef15(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef15", ctApBVO.getVbdef15())); // 业态
					ctApBVO.setVbdef16(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef16", ctApBVO.getVbdef16())); // 车辆部门
					ctApBVO.setVbdef17(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef17", ctApBVO.getVbdef17())); // 部门/楼层
					ctApBVO.setVbdef18(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef18", ctApBVO.getVbdef18())); // 用款人
					// **********成本拆分（合同基本）参照设值
				}
			}
          
			ISuperVO[] leaseconBVOs = billvo.getChildren(LeaseconBVO.class);// 租赁协议
			if (leaseconBVOs != null && leaseconBVOs.length > 0) {
				for (ISuperVO tmpLeaseconBVO : leaseconBVOs) {
					LeaseconBVO leaseconBVO = (LeaseconBVO) tmpLeaseconBVO;
					leaseconBVO.setPk_fct_ap(hpk);
				}
			}

			ISuperVO[] supplyAgreementBVOs = billvo
					.getChildren(SupplyAgreementBVO.class);// 补充协议
			if (supplyAgreementBVOs != null && supplyAgreementBVOs.length > 0) {
				for (ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs) {
					SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO) tmpSupplyAgreementBVO;
					supplyAgreementBVO.setPk_fct_ap(hpk);
				}
			}

			UFDouble sumMny = new UFDouble(0); // 押金金额合计
			ISuperVO[] pMPlanBVOs = billvo.getChildren(PMPlanBVO.class);// 付款计划_保证金_押金_诚意金_共管资金
			if (pMPlanBVOs != null && pMPlanBVOs.length > 0) {
				for (ISuperVO tmpPMPlanBVO : pMPlanBVOs) {
					PMPlanBVO pMPlanBVO = (PMPlanBVO) tmpPMPlanBVO;
					pMPlanBVO.setPk_fct_ap(hpk);
					// 合计押金金额
					UFDouble mpay = pMPlanBVO.getMpay();
					if (mpay != null) {
						sumMny = sumMny.add(mpay);
					}

					// **********付款计划_保证金_押金_诚意金_共管资金参照设值
					pMPlanBVO.setOffsetcompany(fctapConvertor
							.getRefAttributePk("fct_pmplan-offsetcompany",
									pMPlanBVO.getOffsetcompany())); // 抵冲出账公司(付款计划押金)
																	// 参照财务组织*
					pMPlanBVO.setPaytype(fctapConvertor.getRefAttributePk(
							"fct_pmplan-paytype", pMPlanBVO.getPaytype())); // 款项类型-付款计划（保证金、押金、诚意金和共管资金）
					// **********付款计划_保证金_押金_诚意金_共管资金参照设值
				}
			}

			// 表体(付款计划_保证金_押金_诚意金_共管资金)付款金额合计不等于表头押金金额合计，出错
			if (sumMny.toDouble() != Double.parseDouble(billvo.getParentVO()
					.getVdef17())) {
				throw new BusinessException(
						"表体【付款计划_保证金_押金_诚意金_共管资金】付款金额合计不等于表头押金金额合计");
			}

			ISuperVO[] ctApPlanVOs = billvo.getChildren(CtApPlanVO.class);// 付款计划页签（签约金额）
			if (ctApPlanVOs != null && ctApPlanVOs.length > 0) {
				for (ISuperVO tmpCtApPlanVO : ctApPlanVOs) {
					CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
					ctApPlanVO.setPk_fct_ap(hpk);
					ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
					// **********付款计划页签（签约金额）参照设值
					ctApPlanVO.setCompensatecor(fctapConvertor
							.getRefAttributePk("fct_ap_plan-compensatecor",
									ctApPlanVO.getCompensatecor())); // 抵冲出账公司（付款计划签约金额）
																		// 参照财务组织
					ctApPlanVO.setPaymenttype(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-paymenttype",
							ctApPlanVO.getPaymenttype())); // 款项类型-付款计划（签约金额）*
					// **********付款计划页签（签约金额）参照设值
					// 设置款项类型def2
					ctApPlanVO.setDef2(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-def2", ctApPlanVO.getDef2()));
				}
			}

			ISuperVO[] executionBVOs = billvo.getChildren(ExecutionBVO.class);// 执行情况
			if (executionBVOs != null && executionBVOs.length > 0) {
				for (ISuperVO tmpExecutionBVO : executionBVOs) {
					ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
					executionBVO.setPk_fct_ap(hpk);
				}
			}

			ISuperVO[] fctMoretaxs = billvo.getChildren(FctMoretax.class);// 多税率
			if (fctMoretaxs != null && fctMoretaxs.length > 0) {
				for (ISuperVO tmpFctMoretax : fctMoretaxs) {
					FctMoretax fctMoretax = (FctMoretax) tmpFctMoretax;
					fctMoretax.setPk_fct_ap(hpk);
					// **********付款计划页签（签约金额）参照设值
					fctMoretax.setMaininvoicetype(fctapConvertor
							.getRefAttributePk("fct_moretax-maininvoicetype",
									fctMoretax.getMaininvoicetype())); // 主要发票类型
					// **********付款计划页签（签约金额）参照设值
				}
			}
			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;
			if (aggVO == null) {
				// 不存在，新增单据

				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);
				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"SAVEBASE", "FCT1", null, billvo, null, eParam))[0];

				billVO = (AggCtApVO) getMDQryService().queryBillOfVOByPK(
						AggCtApVO.class, billVO.getPrimaryKey(), false);
				eParam.put("nc.bs.scmpub.pf.ORIGIN_VO_PARAMETER",
						new AggCtApVO[] { billVO });

				Object obj = getPfBusiAction().processAction(
						"APPROVE" + getSaleUserID(), "FCT1", null, billVO,
						null, eParam);

				getPfBusiAction().processAction("VALIDATE" + getSaleUserID(),
						"FCT1", null, ((AggCtApVO[]) obj)[0], null, eParam);
			} else {
				// 存在更新单据
                 //TODO 20200814-C
				if("Y".equals(def106)){//详情看上面注释
				 billvo.getParentVO().setDef105(def105);
				}
				//end
				syncBvoPkByEbsPk(CtApPlanVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_plan",
						"fct_ap_plan"); // 同步付款计划（签约金额）PK
				syncBvoPkByEbsPk(PMPlanBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_pmplan_b",
						"fct_pmplan_b");// 同步付款计划_保证金_押金_诚意金_共管资金）PK
				syncBvoPkByEbsPk(CtApBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_b", "fct_ap_b");// 同步成本拆分页签（合同基本）PK
				syncBvoPkByEbsPk(SupplyAgreementBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_supply_agreement_b",
						"fct_supply_agreement_b");// 同步补充协议PK
//				syncBvoPkByEbsPk(ExecutionBVO.class, aggVO.getParentVO()
//						.getPrimaryKey(), billvo, "pk_execution_b",
//						"fct_execution_b");// 同步执行情况PK
				syncBvoPkByEbsPk(FctMoretax.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_moretaxrate",
						"fct_moretax");// 同步多税率PK
				syncBvoPkByEbsPk(LeaseconBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_leasecon_b",
						"fct_leasecon_b");// 同步租赁协议（合同）PK

				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

				// syncBvoTs(CtApPlanVO.class, aggVO, billvo); //
				// 付款计划页签（签约金额）页签ts
				// syncBvoTs(PMPlanBVO.class, aggVO, billvo); //
				// 同步付款计划_保证金_押金_诚意金_共管资金）页签ts
				// syncBvoTs(CtApBVO.class, aggVO, billvo); //
				// 同步成本拆分页签（合同基本）页签ts
				// syncBvoTs(SupplyAgreementBVO.class, aggVO, billvo); //
				// 同步补充协议页签ts
				// syncBvoTs(ExecutionBVO.class, aggVO, billvo); // 同步执行情况页签ts
				// syncBvoTs(FctMoretax.class, aggVO, billvo); // 同步多税率页签ts
				// syncBvoTs(LeaseconBVO.class, aggVO, billvo); //
				// 同步租赁协议（合同）页签ts

				billvo.getParentVO().setTs(aggVO.getParentVO().getTs()); // 同步表头ts
				billvo.getParentVO().setStatus(VOStatus.UPDATED);
				billvo.getParentVO().setPrimaryKey(hpk);
				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

				billVO = ((AggCtApVO[]) getPfBusiAction().processAction(
						"MODIFY", "FCT1", null, billvo, null, eParam))[0];
			}
			// 返回表头单据号和单据PK报文
			dataMap = new HashMap<String, Object>();
			dataMap.put("billid", billVO.getParentVO().getPrimaryKey());
			dataMap.put("billno", billVO.getParentVO().getVbillcode());
             
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(dataMap);

	}

	/**
	 * <p>
	 * Title: syncBvoTs<／p>
	 * <p>
	 * Description: 修改合同时同步表头表体VO的ts<／p>
	 * 
	 * @param clazz
	 *            VOclass
	 * @param srcAggVO
	 *            数据库的VO
	 * @param desAggVO
	 *            传值的VO
	 */
	private void syncBvoTs(Class<? extends ISuperVO> clazz, AggCtApVO srcAggVO,
			AggCtApVO desAggVO) {
		ISuperVO[] sbVOTss = srcAggVO.getChildren(clazz);// 来源单据表体
		// 遍历来源单据表体
		if (sbVOTss != null && sbVOTss.length > 0) {
			for (ISuperVO stmpBVOts : sbVOTss) {
				// 来源表体pk
				String sbvoPk = stmpBVOts.getPrimaryKey();
				// 来源表体ts
				UFDateTime sts = (UFDateTime) stmpBVOts.getAttributeValue("ts");

				ISuperVO[] dbVOTss = desAggVO.getChildren(clazz);// 目标单据表体
				// 遍历目标单据表体
				if (dbVOTss != null && dbVOTss.length > 0) {
					for (int i = 0; i < dbVOTss.length; i++) {
						// 目标表体pk
						String dbvoPk = dbVOTss[i].getPrimaryKey();
						// 来源表体与目标表体pk相同，则是同一表体对象，同步ts
						if (sbvoPk.equals(dbvoPk)) {
							dbVOTss[i].setAttributeValue("ts", sts);
							dbVOTss[i].setStatus(VOStatus.UPDATED);
						}
					}
				}
			}
		}
	}

	/*
	 * private Map<String, Object> fillDataMap(AggCtApVO aggVO){ //
	 * vo与billid、billno的映射 Map<String, Object> voIdNoMap = new HashMap<String,
	 * Object>(); // billid与值、billno与值的映射 Map<String, Object> billIdNoMap =
	 * null;
	 * 
	 * // ***********表头报文 billIdNoMap = new HashMap<String, Object>();
	 * billIdNoMap.put("billid", aggVO.getParentVO().getPrimaryKey());
	 * billIdNoMap.put("billno", aggVO.getParentVO().getVbillcode());
	 * voIdNoMap.put("fct_ap", billIdNoMap); // ***********表头报文
	 * 
	 * // 表体id列表 List<String> bidList = null; // ***********表体付款计划页签（签约金额）报文 //
	 * 表体数组 billIdNoMap = new HashMap<String, Object>(); bidList = new
	 * ArrayList<String>(); ISuperVO[] ctApPlanVOs =
	 * aggVO.getChildren(CtApPlanVO.class); for(ISuperVO tmpCtApPlanVO :
	 * ctApPlanVOs){ CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
	 * bidList.add(ctApPlanVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_ap_plan", billIdNoMap); //
	 * ***********表付款计划页签（签约金额）报文
	 * 
	 * // ***********表付补充协议报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[]
	 * supplyAgreementBVOs = aggVO.getChildren(SupplyAgreementBVO.class);// 补充协议
	 * for(ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs){
	 * SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO)
	 * tmpSupplyAgreementBVO; bidList.add(supplyAgreementBVO.getPrimaryKey()); }
	 * billIdNoMap.put("billid", bidList); voIdNoMap.put("supplyAgreementBVO",
	 * billIdNoMap); // ***********表付补充协议报文
	 * 
	 * // ***********表付成本拆分页签（合同基本）报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] ctApBVOs =
	 * aggVO.getChildren(CtApBVO.class);// 成本拆分页签（合同基本） for(ISuperVO tmpCtApBVO
	 * : ctApBVOs){ CtApBVO ctApBVO = (CtApBVO) tmpCtApBVO;
	 * bidList.add(ctApBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_ap_b", billIdNoMap); //
	 * ***********表付成本拆分页签（合同基本）报文
	 * 
	 * // ***********表付付款计划_保证金_押金_诚意金_共管资金报文 // 表体数组 billIdNoMap = new
	 * HashMap<String, Object>(); bidList = new ArrayList<String>(); ISuperVO[]
	 * pMPlanBVOs = aggVO.getChildren(PMPlanBVO.class);// 付款计划_保证金_押金_诚意金_共管资金
	 * for(ISuperVO tmpPMPlanBVO : pMPlanBVOs){ PMPlanBVO pMPlanBVO =
	 * (PMPlanBVO) tmpPMPlanBVO; bidList.add(pMPlanBVO.getPrimaryKey()); }
	 * billIdNoMap.put("billid", bidList); voIdNoMap.put("fct_pmplan",
	 * billIdNoMap); // ***********表付付款计划_保证金_押金_诚意金_共管资金报文
	 * 
	 * // ***********表付租赁协议报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] leaseconBVOs =
	 * aggVO.getChildren(LeaseconBVO.class);// 租赁协议 for(ISuperVO tmpLeaseconBVO
	 * : leaseconBVOs){ LeaseconBVO leaseconBVO = (LeaseconBVO) tmpLeaseconBVO;
	 * bidList.add(leaseconBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("leaseconBVO", billIdNoMap); //
	 * ***********表付租赁协议报文
	 * 
	 * // ***********表付执行情况报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] executionBVOs =
	 * aggVO.getChildren(ExecutionBVO.class);// 执行情况 for(ISuperVO
	 * tmpExecutionBVO : executionBVOs){ ExecutionBVO executionBVO =
	 * (ExecutionBVO) tmpExecutionBVO;
	 * bidList.add(executionBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_execution", billIdNoMap); //
	 * ***********表付执行情况报文
	 * 
	 * // ***********表付多税率报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] fctMoretaxs =
	 * aggVO.getChildren(FctMoretax.class);// 多税率 for(ISuperVO tmpFctMoretax :
	 * fctMoretaxs){ FctMoretax fctMoretax = (FctMoretax) tmpFctMoretax;
	 * bidList.add(fctMoretax.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_moretax", billIdNoMap); //
	 * ***********表付多税率报文 return voIdNoMap; }
	 */

	/**
	 * <p>
	 * Title: getBvoPkByEbsPK<／p>
	 * <p>
	 * Description: <／p>
	 * 
	 * @param ebsPk
	 *            ebs对应页签数据行主键
	 * @param parentPKfield
	 *            NC付款合同表头主键字段
	 * @param bvoPKfiled
	 *            NC付款合同对应页签主键字段
	 * @param table
	 *            NC付款合同对应页签表名
	 * @param parentPkValue
	 *            NC付款合同表头主键值
	 * @return
	 */
	private Map<String, String> getBvoPkByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select pk_ebs," + bvoPKfiled + " pk from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, String> info = new HashMap<String, String>();
		try {
			parameter.addParam(parentPkValue);
			List<Map<String, String>> list = (List<Map<String, String>>) dao
					.executeQuery(sql, parameter, new MapListProcessor());
			if (list != null && list.size() > 0) {
				for (Map<String, String> map : list) {
					info.put(map.get("pk_ebs"), map.get("pk"));
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * <p>
	 * Title: getBvoPkByEbsPK<／p>
	 * <p>
	 * Description: <／p>
	 * 
	 * @param ebsPk
	 *            ebs对应页签数据行主键
	 * @param parentPKfield
	 *            NC付款合同表头主键字段
	 * @param bvoPKfiled
	 *            NC付款合同对应页签主键字段
	 * @param table
	 *            NC付款合同对应页签表名
	 * @param parentPkValue
	 *            NC付款合同表头主键值
	 * @return
	 */
	private String getBvoPkByEbsPK(String ebsPk, String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String bvoPk = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select " + bvoPKfiled + " from " + table
				+ " where pk_ebs = ? and " + parentPKfield + " = ?";
		try {
			parameter.addParam(ebsPk);
			parameter.addParam(parentPkValue);
			bvoPk = (String) dao.executeQuery(sql, parameter,
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return bvoPk;
	}

	/**
	 * <p>
	 * Title: syncBvoPkByEbsPk<／p>
	 * <p>
	 * Description: <／p>
	 * 
	 * @param clazz
	 *            voClass
	 * @param parentPKValue
	 *            NC付款合同表头主键值
	 * @param desAggVO
	 *            传参过来的aggVO(目标)
	 * @param bvoPKfiled
	 *            vo的主键字段名
	 * @param table
	 *            vo数据表名
	 * @throws DAOException
	 */
	private void syncBvoPkByEbsPk(Class<? extends ISuperVO> clazz,
			String parentPKValue, AggCtApVO desAggVO, String bvoPKfiled,
			String table) throws DAOException {
		// 传参数的BVOs
		ISuperVO[] syncDesPkBVOs = desAggVO.getChildren(clazz);
		Map<String, String> info = getBvoPkByEbsMap("pk_fct_ap", bvoPKfiled,
				table, parentPKValue);
		Map<String, UFDateTime> tsinfo = getBvoTsByEbsMap("pk_fct_ap",
				bvoPKfiled, table, parentPKValue);
		List<String> list = new ArrayList<String>();
		if (info.size() > 0) {
			list.addAll(info.values());
		}
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				// 参数传过来的ebsPK
				String desEBSPk = (String) tmpDesBVO
						.getAttributeValue("pk_ebs");
				// 参数传过来的ebsPK为空则是新增的表体数据，否则是更新的表体数据
				if (desEBSPk != null && !"~".equals(desEBSPk)) {
					String pk = info.get(desEBSPk);
					if (pk != null && !"".equals(pk)) {
						tmpDesBVO.setAttributeValue(bvoPKfiled, pk);
						tmpDesBVO.setAttributeValue("ts", tsinfo.get(pk));
						tmpDesBVO.setStatus(VOStatus.UPDATED);
						list.remove(pk);
					} else {
						tmpDesBVO.setStatus(VOStatus.NEW);
					}

				}

			}
		}

		// 删除原来的数据
		if (list != null && list.size() > 0) {
			String condition = " pk_fct_ap='" + parentPKValue
					+ "' and dr = 0   ";
			String sqlwhere = "";
			for (String value : list) {
				sqlwhere += "'" + value + "',";
			}
			sqlwhere = sqlwhere.substring(0, sqlwhere.length() - 1);
			condition += " and " + bvoPKfiled + "  in(" + sqlwhere + ")";
			Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(clazz,
					condition);
			if (coll != null && coll.size() > 0) {
				List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
				if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
					bodyList.addAll(Arrays.asList(syncDesPkBVOs));
				}
				for (ISuperVO obj : coll) {
					SuperVO vo = (SuperVO) obj;
					vo.setStatus(VOStatus.DELETED);
					vo.setAttributeValue("dr", 1);
					bodyList.add(vo);
				}
				desAggVO.setChildren(clazz, bodyList.toArray(new SuperVO[0]));
			}

		}
	}

	private Map<String, UFDateTime> getBvoTsByEbsMap(String parentPKfield,
			String bvoPKfiled, String table, String parentPkValue) {
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = getBaseDAO();
		sql = "select " + bvoPKfiled + " pk ,ts  from " + table + " where  "
				+ parentPKfield + " = ? and dr =0";
		Map<String, UFDateTime> info = new HashMap<String, UFDateTime>();
		try {
			parameter.addParam(parentPkValue);
			List<Map<String, Object>> list = (List<Map<String, Object>>) dao
					.executeQuery(sql, parameter, new MapListProcessor());
			if (list != null && list.size() > 0) {
				for (Map<String, Object> map : list) {
					info.put((String) map.get("pk"), new UFDateTime(
							(String) map.get("ts")));
				}
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return info;
	}
}
