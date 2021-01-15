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
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IPfExchangeService;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.fct.ap.entity.CtApBVO;
import nc.vo.fct.ap.entity.CtApPlanVO;
import nc.vo.fct.ap.entity.CtApVO;
import nc.vo.fct.ap.entity.ExecutionBVO;
import nc.vo.fct.ap.entity.OutPutBVO;
import nc.vo.fct.ap.entity.SupplyAgreementBVO;
import nc.vo.fct.ar.entity.AggCtArVO;
import nc.vo.fct.ar.entity.ArOutPutBVO;
import nc.vo.fct.ar.entity.CtArBVO;
import nc.vo.fct.ar.entity.CtArVO;
import nc.vo.gateway60.accountbook.GlOrgUtils;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FctapCostBillUtils extends EBSBillUtils {
	static FctapCostBillUtils utils;

	public static FctapCostBillUtils getUtils() {
		if (utils == null) {
			utils = new FctapCostBillUtils();
		}
		return utils;
	}

	/**
	 * VO转换服务类
	 * 
	 * @return
	 */
	private IPfExchangeService ips = null;

	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}

	/**
	 * 付款合同-成本合同保存
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

		String vbillcode = headJSON.getString("vbillcode");// 合同编号(ebs合同编码)
		String pk = headJSON.getString("def49");// ebs表头合同主键

		// 目标单据名+请求合同编号作队列
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
					"isnull(dr,0)=0 and blatest ='Y' and def49 = '" + pk + "'");
			String hpk = null;
			String def103=null;//：“从成本台账中剔除”（表头def103）、“包含在成本台账中”(表头def104)
			String def104=null;
			String def106=null;//单据是否走财务变更按钮修改数据标志（如果为Y，def103,def104取原值）
			if (aggVO != null) {
				hpk = aggVO.getParentVO().getPrimaryKey();
				def103=aggVO.getParentVO().getDef103();
				def104=aggVO.getParentVO().getDef104();
				def106=aggVO.getParentVO().getDef106();
			}
			// 数据转换工具对象
			FctapCostConvertor fctapConvertor = new FctapCostConvertor();
			// 配置表头key与名称映射
			Map<String, String> hVOKeyName = new HashMap<String, String>();
			hVOKeyName.put("fct_ap", "付款合同");
			fctapConvertor.setHVOKeyName(hVOKeyName);

			// 配置表体key与名称映射
			Map<String, String> bVOKeyName = new HashMap<String, String>();
			bVOKeyName.put("fct_ap_plan", "付款计划页签（签约金额）");
			bVOKeyName.put("supplyAgreementBVO", "补充协议");
			bVOKeyName.put("fct_ap_b", "成本拆分页签（合同基本）");
			bVOKeyName.put("fct_execution", "执行情况");
			bVOKeyName.put("OutPutBVO", "产值");
			fctapConvertor.setBVOKeyName(bVOKeyName);

			// 配置表头检验字段
			Map<String, Map<String, String>> hValidatedKeyName = new HashMap<String, Map<String, String>>();
			Map<String, String> hKeyName = new HashMap<String, String>();
			// hKeyName.put("pk_org", "财务组织(城市公司)");
			// hKeyName.put("subscribedate", "签字盖章日期");
			// hKeyName.put("cvendorid", "供应商");
			// hKeyName.put("plate", "板块");
			hKeyName.put("contype", "合同类型");
			hKeyName.put("vbillcode", "合同编码");
			// hKeyName.put("def51", "NC入账发票金额");
			hKeyName.put("def52", "合同状态");
			hKeyName.put("def56", "是否通用合同引入");
			hKeyName.put("ctname", "合同名称");
			hKeyName.put("proname", "项目名称");
			hKeyName.put("first", "甲方");
			hKeyName.put("second", "乙方");
			// hKeyName.put("vdef19", "合同属性");
			// hKeyName.put("d_sign", "签约日期");
			// hKeyName.put("vdef4", "承包方式");
			hKeyName.put("ntotalorigmny", "签约金额");
			hKeyName.put("rate", "税率");
			// hKeyName.put("depid", "经办部门");
			// hKeyName.put("vdef1", "业态");
			hKeyName.put("vdef15", "结算状态");
			// hKeyName.put("vdef2", "业态比例");
			hKeyName.put("vdef5", "财务成本标记");
			// hKeyName.put("vdef6", "累计变更金额");
			// hKeyName.put("vdef7", "累计补充协议金额");
			// hKeyName.put("vdef8", "结算调整金额");
			// hKeyName.put("vdef16", "结算金额");
			// hKeyName.put("vdef9", "动态金额（含税）");
			// hKeyName.put("vdef10", "动态金额（不含税）");
			// hKeyName.put("vdef11", "动态金额（税额）");
			// hKeyName.put("vdef18", "发票金额（含税）");
			// hKeyName.put("vdef21", "EBS实付款(含税)");
			// hKeyName.put("def31", "累计应付款");
			// hKeyName.put("def40", "累计已请款");
			hKeyName.put("def41", "EBS累计实付款(含税)");
			// hKeyName.put("def42", "累计实付款（不含税）");
			// hKeyName.put("def43", "累计实付款（税额）");
			// hKeyName.put("def44", "未付款 （含税）");
			// hKeyName.put("def45", "未付款 （不含税）");
			// hKeyName.put("def46", "未付款 （税额）");

			hValidatedKeyName.put("fct_ap", hKeyName);
			fctapConvertor.sethValidatedKeyName(hValidatedKeyName);

			// 配置表体检验字段
			Map<String, Map<String, String>> bValidatedKeyName = new HashMap<String, Map<String, String>>();
			// **********执行情况页签字段检验
			Map<String, String> bExecutionBVOKeyName = new HashMap<String, String>();
			// bExecutionBVOKeyName.put("pk_ebs", "ebs主键");
			// bExecutionBVOKeyName.put("billno", "付款申请号");
			// bExecutionBVOKeyName.put("d_apply", "申请日期");
			// bExecutionBVOKeyName.put("mapply", "申请金额");
			// bExecutionBVOKeyName.put("mpay", "已付金额");
			// bExecutionBVOKeyName.put("paydate", "付款（支付）日期");
			// bExecutionBVOKeyName.put("payway", "支付方式");

			bValidatedKeyName.put("fct_execution", bExecutionBVOKeyName);
			// **********执行情况页签字段检验

			// **********付款计划页签字段检验
			Map<String, String> bCtApPlanVOKeyName = new HashMap<String, String>();
			// bCtApPlanVOKeyName.put("pk_ebs", "ebs主键");
			// bCtApPlanVOKeyName.put("d_payplan", "计划付款日期");
			// bCtApPlanVOKeyName.put("planrate", "计划付款比例");
			// bCtApPlanVOKeyName.put("planmoney", "计划付款金额");
			// bCtApPlanVOKeyName.put("def2", "款项类型");
			// bCtApPlanVOKeyName.put("def1", "是否支付到共管户");
			bValidatedKeyName.put("fct_ap_plan", bCtApPlanVOKeyName);
			// **********付款计划页签字段检验

			// **********补充协议页签字段检验
			Map<String, String> bSupplyAgreementBVOKeyName = new HashMap<String, String>();
			// bSupplyAgreementBVOKeyName.put("pk_ebs", "ebs主键");
			// bSupplyAgreementBVOKeyName.put("billno", "协议编号");
			// bSupplyAgreementBVOKeyName.put("name", "协议名称");
			// bSupplyAgreementBVOKeyName.put("mmonery", "协议金额");
			// bSupplyAgreementBVOKeyName.put("d_date", "签约日期");
			// bSupplyAgreementBVOKeyName.put("msupply", "协议金额累计");
			bValidatedKeyName.put("supplyAgreementBVO",
					bSupplyAgreementBVOKeyName);
			// **********补充协议页签字段检验

			// **********产值页签字段检验
			Map<String, String> bOutPutBVOKeyName = new HashMap<String, String>();
			// bOutPutBVOKeyName.put("pk_ebs", "ebs主键");
			// bOutPutBVOKeyName.put("mcz", "累计产值_含税");
			// bOutPutBVOKeyName.put("mczbhs", "累计产值_不含税");
			// bOutPutBVOKeyName.put("mczse", "累计产值_税额");
			// bOutPutBVOKeyName.put("mwfcz", "累计未付产值_含税");
			// bOutPutBVOKeyName.put("mwfczbhs", "累计未付产值_不含税");
			// bOutPutBVOKeyName.put("mwfczse", "累计未付产值_税额");
			// bOutPutBVOKeyName.put("outputtime", "产值时间");
			// bOutPutBVOKeyName.put("m_supply", "协议金额累计");
			bValidatedKeyName.put("OutPutBVO", bOutPutBVOKeyName);
			// **********产值页签字段检验

			// **********合同基本（成本拆分）页签字段检验
			Map<String, String> bCtApBVOKeyName = new HashMap<String, String>();
			// bCtApBVOKeyName.put("pk_ebs", "ebs主键");
			// bCtApBVOKeyName.put("vbdef11", "科目名称");
			// bCtApBVOKeyName.put("vbdef23", "科目编码");
			// bCtApBVOKeyName.put("vbdef22", "产品类型—业态");
			// bCtApBVOKeyName.put("vbdef13", "拆分比例");
			// bCtApBVOKeyName.put("vbdef21", "拆分金额");
			bValidatedKeyName.put("fct_ap_b", bCtApBVOKeyName);
			// **********合同基本（成本拆分）页签字段检验
			fctapConvertor.setbValidatedKeyName(bValidatedKeyName);

			// 配置参照字段
			List<String> refKeys = new ArrayList<String>();
			refKeys.add("fct_ap-plate"); // 板块 -- 板块*
			refKeys.add("fct_ap-proname"); // 项目名称 -- 项目*
			refKeys.add("fct_ap-pk_org"); // 财务组织（城市公司）*
			refKeys.add("fct_ap-depid");// 经办部门 -- 部门*
			refKeys.add("fct_ap-first");// 甲方单位 -- 财务组织*
			refKeys.add("fct_ap-second");// 乙方单位 -- 供应商*
			refKeys.add("fct_ap-vdef1");// 业态 -- 业态
			refKeys.add("fct_ap_plan-def2");// 款项类型-付款计划（签约金额）
			refKeys.add("fct_ap_b-vbdef11");// 科目名称（预算科目）-合同基本（成本拆分）
			// refKeys.add("fct_ap-vdef4");// 承包方式 -- 承包方式
			fctapConvertor.setRefKeys(refKeys);

			// 默认集团-时代集团
			fctapConvertor.setDefaultGroup("000112100000000005FD");
			AggCtApVO billvo = (AggCtApVO) fctapConvertor.castToBill(value,
					AggCtApVO.class, aggVO);

			// 设置合同单据默认信息

			// ********************表头参照信息设置***************

			String first = fctapConvertor.getRefAttributePk("fct_ap-first",
					billvo.getParentVO().getFirst());
			billvo.getParentVO().setFirst(first); // 甲方（财务组织）

			// billvo.getParentVO().setPk_org(
			// fctapConvertor.getRefAttributePk("fct_ap-pk_org", billvo
			// .getParentVO().getFirst())); // 财务组织
			billvo.getParentVO().setPk_org(first);

			billvo.getParentVO().setPk_org_v(
					GlOrgUtils.getPkorgVersionByOrgID(billvo.getParentVO()
							.getFirst()));

			billvo.getParentVO().setPlate(
					fctapConvertor.getRefAttributePk("fct_ap-plate", billvo
							.getParentVO().getPlate())); // 板块
			billvo.getParentVO().setProname(
					fctapConvertor.getRefAttributePk("fct_ap-proname", billvo
							.getParentVO().getProname(), billvo.getParentVO()
							.getFirst(), billvo.getParentVO().getFirst())); // 项目名称（项目）
			billvo.getParentVO().setSecond(
					fctapConvertor.getRefAttributePk("fct_ap-second", billvo
							.getParentVO().getSecond(), billvo.getParentVO()
							.getPk_org(), billvo.getParentVO().getPk_org()));// 乙方（供应商）
			// billvo.getParentVO().setDepid(fctapConvertor.getRefAttributePk("fct_ap-depid",
			// billvo.getParentVO().getDepid(),
			// billvo.getParentVO().getPk_org())); // 经办（承办）部门
			billvo.getParentVO().setVdef1(
					fctapConvertor.getRefAttributePk("fct_ap-vdef1", billvo
							.getParentVO().getVdef1())); // 业态
			// ********************表头参照信息设置***************

			// 表头默认信息配置
			billvo.getParentVO().setCtrantypeid(
					fctapConvertor.getRefAttributePk("ctrantypeid",
							"FCT1-Cxx-001")); // 交易类型
			billvo.getParentVO().setCbilltypecode("FCT1"); // 单据类型
			billvo.getParentVO().setValdate(new UFDate()); // 计划生效日期
			billvo.getParentVO().setInvallidate(new UFDate());// 计划终止日期
			billvo.getParentVO().setNexchangerate(new UFDouble(100));// 折本汇率
			billvo.getParentVO().setSubscribedate(new UFDate());// 盖章签字日期
			UFDouble ntotalorigmny = (UFDouble) billvo.getParent()
					.getAttributeValue("ntotalorigmny");
			billvo.getParentVO().setMsign(ntotalorigmny);// 签约金额

			/**
			 * 城市公司通过甲方带出来2019-10-24-tzj
			 */
			String vdef14 = fctapConvertor.getRefAttributePk("fct_ap-pk_org",
					billvo.getParentVO().getFirst());
			billvo.getParentVO().setVdef14(vdef14);
			// 通过vdef12编码带出人员2020-02-17-谈子健
			String vdef12 = fctapConvertor.getRefAttributePk("fct_ap-vdef12",
					billvo.getParentVO().getVdef12());
			billvo.getParentVO().setVdef12(vdef12);
			// // 城市公司（财务组织）

			billvo.getParentVO().setCvendorid(billvo.getParentVO().getSecond());// 乙方（供应商）

			// 新增字段2019-10-30-tzj
			// billvo.getParentVO().setDef51(billvo.getParentVO().getDef51());//
			// NC入账发票金额
			billvo.getParentVO().setDef41(billvo.getParentVO().getDef41());// EBS累计实付款(含税)
			billvo.getParentVO().setDef52(billvo.getParentVO().getDef52());// 合同状态
			billvo.getParentVO().setDef53(billvo.getParentVO().getDef53());// 内部结算金额
			billvo.getParentVO().setDef54(billvo.getParentVO().getDef54());// 总包合同编码
			billvo.getParentVO().setDef55(billvo.getParentVO().getDef55());// 总包合同名称
			billvo.getParentVO().setDef56(billvo.getParentVO().getDef56());// 是否通用合同引用
			billvo.getParentVO().setDef58(billvo.getParentVO().getDef58());// 经办人编码
			billvo.getParentVO().setDef61(billvo.getParentVO().getDef61());// def61
																			// NC期初发票金额（含税）
			billvo.getParentVO().setDef62(billvo.getParentVO().getDef62());// def62
																			// NC期初发票金额（不含税）
			billvo.getParentVO().setDef63(billvo.getParentVO().getDef63());// def63
																			// NC期初发票金额（税额）
			billvo.getParentVO().setDef96(billvo.getParentVO().getDef96());// def96
																			// NC期初实付款（不含税）
			billvo.getParentVO().setDef97(billvo.getParentVO().getDef97());// def97
																			// NC期初实付款（税额）
			billvo.getParentVO().setDef98(billvo.getParentVO().getDef98());// def98
																			// NC期初实付款（含税）
			billvo.getParentVO().setDef99(billvo.getParentVO().getDef99());// def99
																			// 是否自动扣款
			String def100 = fctapConvertor.getRefAttributePk("fct_ap-def100",
					billvo.getParentVO().getDef100());
			billvo.getParentVO().setDef100(def100);// def100 划扣类型
			billvo.getParentVO().setDef79(billvo.getParentVO().getDef79());// ebs版本主键
			if (billvo.getParentVO().getDef101() != null
					&& !"".equals(billvo.getParentVO().getDef101())) {
				String def101 = fctapConvertor.getRefAttributePk(
						"fct_ap-def101", billvo.getParentVO().getDef101());
				billvo.getParentVO().setDef101(def101);// def101 业务部门
			}
			billvo.getParentVO().setDef102(billvo.getParentVO().getDef102());// def102
																				// 付款银行账户
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
					ctApBVO.setAttributeValue("ntaxmny", 1); // 本币价税合计
					ctApBVORowNo += 10;

					// **********成本拆分（合同基本）参照设值
					ctApBVO.setVbdef11(fctapConvertor.getRefAttributePk(
							"fct_ap_b-vbdef11", ctApBVO.getVbdef11())); // 科目名称（预算科目）
					// **********成本拆分（合同基本）参照设值
				}
			}
			// else {
			// throw new BusinessException("成本拆分页签(合同基本)页签不能为空!");
			// }

			ISuperVO[] supplyAgreementBVOs = billvo
					.getChildren(SupplyAgreementBVO.class);// 补充协议
			if (supplyAgreementBVOs != null && supplyAgreementBVOs.length > 0) {
				for (ISuperVO tmpSupplyAgreementBVO : supplyAgreementBVOs) {
					SupplyAgreementBVO supplyAgreementBVO = (SupplyAgreementBVO) tmpSupplyAgreementBVO;
					supplyAgreementBVO.setPk_fct_ap(hpk);
					supplyAgreementBVO.setStatus(VOStatus.NEW);
				}
			}

			ISuperVO[] ctApPlanVOs = billvo.getChildren(CtApPlanVO.class);// 付款计划页签（签约金额）
			if (ctApPlanVOs != null && ctApPlanVOs.length > 0) {
				for (ISuperVO tmpCtApPlanVO : ctApPlanVOs) {
					CtApPlanVO ctApPlanVO = (CtApPlanVO) tmpCtApPlanVO;
					ctApPlanVO.setPk_fct_ap(hpk);
					ctApPlanVO.setPk_org(billvo.getParentVO().getPk_org());
					ctApPlanVO.setPk_group(billvo.getParentVO().getPk_group());
					// **********付款计划页签（签约金额）参照设值
					ctApPlanVO.setPaymenttype(fctapConvertor.getRefAttributePk(
							"fct_ap_plan-paymenttype",
							ctApPlanVO.getPaymenttype())); // 款项类型-付款计划（签约金额）*
					// **********付款计划页签（签约金额）参照设值
					ctApPlanVO.setStatus(VOStatus.NEW);
				}
			}

			ISuperVO[] executionBVOs = billvo.getChildren(ExecutionBVO.class);// 执行情况
			if (executionBVOs != null && executionBVOs.length > 0) {
				for (ISuperVO tmpExecutionBVO : executionBVOs) {

					ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
					// 【执行情况】fct_execution页签“支付方式”payway，nc需做映射关系-2020-02-17-谈子健
					String payway = executionBVO.getPayway();
					String pk_balatype = getPayWay(payway);
					executionBVO.setPayway(pk_balatype);
					executionBVO.setPk_fct_ap(hpk);
					executionBVO.setStatus(VOStatus.NEW);

				}
			}

			ISuperVO[] outPutBVOs = billvo.getChildren(OutPutBVO.class);// 产值
			if (outPutBVOs != null && outPutBVOs.length > 0) {
				for (ISuperVO tmpOutPutBVO : outPutBVOs) {
					OutPutBVO outPutBVO = (OutPutBVO) tmpOutPutBVO;
					outPutBVO.setPk_fct_ap(hpk);
					outPutBVO.setStatus(VOStatus.NEW);
				}
			}

			HashMap<String, Object> eParam = new HashMap<String, Object>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			AggCtApVO billVO = null;
			if (aggVO == null) {
				// 不存在，新增单据blatest
				billvo.getParentVO().setCreator(getSaleUserID());
				billvo.getParentVO().setBillmaker(getSaleUserID());
				billvo.getParentVO().setCreationtime(new UFDateTime());
				billvo.getParentVO().setDmakedate(new UFDate());
				billvo.getParentVO().setFstatusflag(0);
				billvo.getParentVO().setVersion(UFDouble.ONE_DBL);
				// billvo.getParentVO().setBlatest(UFBoolean.TRUE);
				// NC期初实付款 第一次默认等于EBS累计实付款
				billvo.getParentVO().setVdef22(billvo.getParentVO().getDef41());
				String def98 = billvo.getParentVO().getDef98();

				billvo = getCount(def98, executionBVOs, billvo);

				// 奖励金额
				String vdef23 = billvo.getParentVO().getVdef23();
				billvo.getParentVO().setVdef23(vdef23);
				// 扣款金额
				String vdef24 = billvo.getParentVO().getVdef24();
				billvo.getParentVO().setVdef24(vdef24);

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
				
				billvo = getCount(aggVO.getParentVO().getDef98(),
						executionBVOs, billvo);
			//TODO 20200814-C
				if("Y".equals(def106)){
					billvo.getParentVO().setDef103(def103);
					billvo.getParentVO().setDef104(def104);
				}
				// 存在更新单
				syncBvoPkByEbsPk(CtApPlanVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_plan",
						"fct_ap_plan"); // 同步付款计划（签约金额）PK
				syncBvoPkByEbsPk(CtApBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_fct_ap_b", "fct_ap_b");// 同步成本拆分页签（合同基本）PK
				syncBvoPkByEbsPk(SupplyAgreementBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_supply_agreement_b",
						"fct_supply_agreement_b");// 同步补充协议PK
				// syncBvoPkByEbsPk(ExecutionBVO.class, aggVO.getParentVO()
				// .getPrimaryKey(), billvo, "pk_execution_b",
				// "fct_execution_b");// 同步执行情况PK
				syncBvoPkByEbsPk(OutPutBVO.class, aggVO.getParentVO()
						.getPrimaryKey(), billvo, "pk_output", "fct_output_b");// 同步产值PK

				billvo.getParentVO().setTs(aggVO.getParentVO().getTs()); // 同步表头ts
				billvo.getParentVO().setStatus(VOStatus.UPDATED);
				billvo.getParentVO().setPrimaryKey(hpk);

				billvo.getParentVO().setModifier(getSaleUserID());
				billvo.getParentVO().setModifiedtime(new UFDateTime());

				syncBvoTs(CtApPlanVO.class, aggVO, billvo); // 付款计划页签（签约金额）页签ts
				syncBvoTs(CtApBVO.class, aggVO, billvo); // 同步成本拆分页签（合同基本）页签ts
				syncBvoTs(SupplyAgreementBVO.class, aggVO, billvo); // 同步补充协议页签ts
				// syncBvoTs(ExecutionBVO.class, aggVO, billvo); // 同步执行情况页签ts
				syncBvoTs(OutPutBVO.class, aggVO, billvo); // 同步产值页签ts
				// 把旧的执行情况删除2019-11-27-谈子健
				deleteExecution(billvo);
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
	 * // ***********表付执行情况报文 // 表体数组 billIdNoMap = new HashMap<String,
	 * Object>(); bidList = new ArrayList<String>(); ISuperVO[] executionBVOs =
	 * aggVO.getChildren(ExecutionBVO.class);// 执行情况 for(ISuperVO
	 * tmpExecutionBVO : executionBVOs){ ExecutionBVO executionBVO =
	 * (ExecutionBVO) tmpExecutionBVO;
	 * bidList.add(executionBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("fct_execution", billIdNoMap); //
	 * ***********表体执行情况报文
	 * 
	 * // ***********表体产值报文 // 表体数组 billIdNoMap = new HashMap<String, Object>();
	 * bidList = new ArrayList<String>(); ISuperVO[] outPutBVOs =
	 * aggVO.getChildren(OutPutBVO.class);// 产值 for(ISuperVO tmpOutPutBVO :
	 * outPutBVOs){ OutPutBVO outPutBVO = (OutPutBVO) tmpOutPutBVO;
	 * bidList.add(outPutBVO.getPrimaryKey()); } billIdNoMap.put("billid",
	 * bidList); voIdNoMap.put("OutPutBVO", billIdNoMap); // ***********表体产值报文
	 * return voIdNoMap; }
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
		if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
			for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
				// 参数传过来的ebsPK
				String desEBSPk = (String) tmpDesBVO
						.getAttributeValue("pk_ebs");
				// 判断是否是产值页签
				if (desEBSPk == null && "fct_output_b".equals(table)) {
					String sql = "select b.outputtime,b.pk_output from fct_output_b b where b.pk_fct_ap = '"
							+ parentPKValue + "' and b.dr = 0";

					List<Map<String, String>> outputtimeMap = (List<Map<String, String>>) getBaseDAO()
							.executeQuery(sql, new MapListProcessor());
					String time = (String) tmpDesBVO.getAttributeValue(
							"outputtime").toString();
					for (Map<String, String> map : outputtimeMap) {
						String outputtime = map.get("outputtime");
						String pk_output = map.get("pk_output");
						if (time != null && !"".equals(time)) {
							if (time.equals(outputtime)) {
								tmpDesBVO.setAttributeValue(bvoPKfiled,
										pk_output);
								tmpDesBVO.setStatus(VOStatus.UPDATED);
							} else {
								tmpDesBVO.setStatus(VOStatus.NEW);
							}
						}
					}
				} else {
					// 参数传过来的ebsPK为空则是新增的表体数据，否则是更新的表体数据
					tmpDesBVO.setAttributeValue(
							bvoPKfiled,
							getBvoPkByEbsPK(desEBSPk, "pk_fct_ap", bvoPKfiled,
									table, parentPKValue));
					if (tmpDesBVO.getPrimaryKey() == null) {
						tmpDesBVO.setStatus(VOStatus.NEW);
					} else {
						tmpDesBVO.setStatus(VOStatus.UPDATED);

					}
				}

			}
		}
	}

	// 表头金额计算
	private AggCtApVO getCount(String def98, ISuperVO[] executionBVOs,
			AggCtApVO billvo) {
		UFDouble def98Double = new UFDouble(def98);
		UFDouble mpay = new UFDouble();
		if (executionBVOs != null) {
			for (ISuperVO tmpExecutionBVO : executionBVOs) {
				ExecutionBVO executionBVO = (ExecutionBVO) tmpExecutionBVO;
				UFDouble m = executionBVO.getMpay();
				if (m != null) {
					mpay = mpay.add(m);
				}

			}
		}
		if (mpay != null) {
			UFDouble sun = mpay.add(def98Double);
			//UFDouble sun = mpay;
			if (sun != null) {
				String vdef21 = sun.toString();
				// NC累计实付款（含税）= NC期初实付款+NC累计发生
				billvo.getParentVO().setVdef21(vdef21);

				// NC未付款 （含税）def44=def40-vdef21
				UFDouble def44 = new UFDouble(billvo.getParentVO().getDef40())
						.sub(sun);
				billvo.getParentVO().setDef44(
						def44 == null ? "0" : def44.toString());

				// 累计欠票=NC累计实付含税-BES累计发票金额 def42=vdef21-vdef1
				UFDouble def42 = sun.sub(new UFDouble(billvo.getParentVO()
						.getVdef18()));
				billvo.getParentVO().setDef42(
						def42 == null ? "0" : def42.toString());

				// UFDouble rate = billvo.getParentVO().getRate();
				// UFDouble rateUFDouble = new UFDouble();
				// if (rate != null) {
				// rateUFDouble = new UFDouble(rate).div(100).add(1);
				// // NC累计实付款（不含税）= NC累计实付款（含税）/(1+税率/100)
				// String def42 = sun.div(rateUFDouble).toString();
				// billvo.getParentVO().setDef42(def42);
				// // NC累计实付款（税额）= (累计实付款（含税）/(1+税率/100))*税率/100
				// String def43 = sun.div(rateUFDouble)
				// .multiply(rate.div(100)).toString();
				// billvo.getParentVO().setDef43(def43);
				// }
				// String vdef9 = billvo.getParentVO().getVdef9();
				// if (vdef9 != null && !"".equals(vdef9)) {
				// UFDouble vdef9UFDouble = new UFDouble(vdef9);
				// // NC未付款 （含税） = 动态金额（含税）-NC累计实付款（含税）
				// String def44 = vdef9UFDouble.sub(sun).toString();
				// billvo.getParentVO().setDef44(def44);
				// // NC未付款 （不含税） = 未付款（含税）/(1+税率/100)
				// if (rateUFDouble != null) {
				// UFDouble def45 = vdef9UFDouble.sub(sun).div(
				// rateUFDouble);
				// billvo.getParentVO().setDef45(def45.toString());
				// // NC未付款 （税额） = 未付款（含税）*(rate/100)
				// UFDouble def46 = def45.multiply(new UFDouble(rate)
				// .div(100));
				// billvo.getParentVO().setDef46(def46.toString());
				// }
				// }
			}

		}

		return billvo;

	}

	/**
	 * 把旧的执行情况删除2019-11-27-谈子健
	 * 
	 * @param billvo
	 * @throws DAOException
	 */
	private void deleteExecution(AggCtApVO billvo) throws DAOException {
		String primaryKey = billvo.getParent().getPrimaryKey();
		ISuperVO[] syncDesPkBVOs = billvo.getChildren(ExecutionBVO.class);
		BaseDAO dao = getBaseDAO();
		List<String> pkList = (List<String>) dao.executeQuery(
				"select b.pk_execution_b from fct_execution_b b where b.pk_fct_ap = '"
						+ primaryKey + "' and b.dr = 0",
				new ColumnListProcessor());
		StringBuffer sb = new StringBuffer();
		if (pkList != null && pkList.size() > 0) {
			for (String pk_execution_b : pkList) {
				sb.append("'");
				sb.append(pk_execution_b);
				sb.append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			String condition = " pk_fct_ap='" + primaryKey
					+ "' and dr = 0  and pk_execution_b in (" + sb.toString()
					+ ")";

			Collection<ISuperVO> coll = getBaseDAO().retrieveByClause(
					ExecutionBVO.class, condition);
			if (coll != null && coll.size() > 0) {
				List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
				if (syncDesPkBVOs != null && syncDesPkBVOs.length > 0) {
					for (ISuperVO tmpDesBVO : syncDesPkBVOs) {
						tmpDesBVO.setStatus(VOStatus.NEW);
					}
					bodyList.addAll(Arrays.asList(syncDesPkBVOs));
					for (ISuperVO obj : coll) {
						SuperVO vo = (SuperVO) obj;
						vo.setStatus(VOStatus.DELETED);
						vo.setAttributeValue("dr", 1);
						bodyList.add(vo);
					}
					billvo.setChildren(ExecutionBVO.class,
							bodyList.toArray(new SuperVO[0]));
				}
			}

		}
	}

	public String getPayWay(String payway) throws BusinessException {
		BaseDAO dao = getBaseDAO();
		String pk_balatype = "";
		if (payway != null && !"".equals(payway)) {
			switch (payway) {
			case "10":
				payway = "9";// ABS/ABN
				break;
			case "20":
				payway = "10";// 现金
				break;
			case "30":
				payway = "4";// 商票
				break;
			case "40":
				payway = "8";// 抵楼(无现金)
				break;
			case "50":
				payway = "1";// 网银
				break;
			case "60":
				payway = "2";// 银企直联
				break;
			case "70":
				payway = "3";// 支票
				break;
			case "80":
				payway = "6";// 银行汇款
				break;
			case "90":
				payway = "7";// 电汇单
				break;
			case "100":
				payway = "5";// 商票(纸质)
				break;
			}
			pk_balatype = (String) dao.executeQuery(
					"select t.pk_balatype from bd_balatype t where t.code = '"
							+ payway + "' and t.dr = 0 and t.enablestate = 2 ",
					new ColumnProcessor());
		}
		return pk_balatype;
	}
}
