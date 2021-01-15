package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class EBSCont {
	// ------------------档案对照-------------------------
	// TODO 部门、人员、岗位引用HCM档案,不必发布接口

	private static Map<String, String> docNameMap = null;

	public static final String DOC_01 = "01"; // 项目档案

	public static final String DOCNAME_01 = "项目档案"; // 项目档案

	public static final String DOC_02 = "02"; // 项目公司

	public static final String DOCNAME_02 = "项目公司"; // 项目公司

	public static final String DOC_03 = "03"; // 合同类型

	public static final String DOCNAME_03 = "合同类型"; // 合同类型

	public static final String DOC_04 = "04"; // 经办部门

	public static final String DOCNAME_04 = "经办部门"; // 经办部门

	public static final String DOC_05 = "05"; // 经办人

	public static final String DOCNAME_05 = "经办人"; // 经办人

	public static final String DOC_06 = "06"; // 经办岗位

	public static final String DOCNAME_06 = "经办岗位"; // 经办岗位

	public static final String DOC_07 = "07"; // 合作伙伴

	public static final String DOCNAME_07 = "合作伙伴"; // 合作伙伴

	public static final String DOC_08 = "08"; // 税率

	public static final String DOCNAME_08 = "税率"; // 税率

	public static final String DOC_09 = "09"; // 承包方式

	public static final String DOCNAME_09 = "承包方式"; // 承包方式

	public static final String DOC_10 = "10"; // 业态

	public static final String DOCNAME_10 = "业态"; // 业态

	public static final String DOC_11 = "11"; // 款项类型

	public static final String DOCNAME_11 = "款项类型"; // 款项类型

	public static final String DOC_12 = "12"; // 预算科目
	public static final String DOCNAME_12 = "预算科目"; // 预算科目

	public static final String DOC_13 = "13"; // 成本科目
	public static final String DOCNAME_13 = "成本科目"; // 成本科目

	public static final String DOC_14 = "14"; // 收支项目
	public static final String DOCNAME_14 = "收支项目"; // 收支项目

	public static final String DOC_15 = "15"; // 板块信息
	public static final String DOCNAME_15 = "板块信息"; // 板块信息

	public static final String DOC_16 = "16"; // 银行档案
	public static final String DOCNAME_16 = "银行档案"; // 银行档案

	public static final String DOC_17 = "17"; // 银行账户
	public static final String DOCNAME_17 = "银行账户"; // 银行账户

	public static final String DOC_18 = "18"; // 结算方式
	public static final String DOCNAME_18 = "结算方式"; // 结算方式

	public static final String DOC_19 = "19"; // 车辆部门
	public static final String DOCNAME_19 = "车辆部门"; // 车辆部门

	public static final String DOC_20 = "20"; // 部门楼层
	public static final String DOCNAME_20 = "部门楼层"; // 部门楼层

	public static final String DOC_21 = "21"; // 供应商
	public static final String DOCNAME_21 = "供应商"; // 供应商
	
	public static final String DOC_22 = "22"; // 个人银行账号
	public static final String DOCNAME_22 = "个人银行账户"; // 个人银行账号

	/**
	 * 目标业务单据中文名对照 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getDocNameMap() {
		if (docNameMap == null) {
			docNameMap = new HashMap<String, String>();
			docNameMap.put(DOC_01, DOCNAME_01);
			docNameMap.put(DOC_02, DOCNAME_02);
			docNameMap.put(DOC_03, DOCNAME_03);
			docNameMap.put(DOC_04, DOCNAME_04);
			docNameMap.put(DOC_05, DOCNAME_05);
			docNameMap.put(DOC_06, DOCNAME_06);
			docNameMap.put(DOC_07, DOCNAME_07);
			docNameMap.put(DOC_08, DOCNAME_08);
			docNameMap.put(DOC_09, DOCNAME_09);
			docNameMap.put(DOC_10, DOCNAME_10);
			docNameMap.put(DOC_11, DOCNAME_11);
			docNameMap.put(DOC_12, DOCNAME_12);
			docNameMap.put(DOC_13, DOCNAME_13);
			docNameMap.put(DOC_14, DOCNAME_14);
			docNameMap.put(DOC_15, DOCNAME_15);
			docNameMap.put(DOC_16, DOCNAME_16);
			docNameMap.put(DOC_17, DOCNAME_17);
			docNameMap.put(DOC_18, DOCNAME_18);
			docNameMap.put(DOC_19, DOCNAME_19);
			docNameMap.put(DOC_20, DOCNAME_20);
			docNameMap.put(DOC_21, DOCNAME_21);
		}
		return docNameMap;
	}

	// ------------------表体对照-----------------------
	private static Map<String, String> billNameMap = null;

	public static final String BILL_01 = "01"; // 费用申请单（合同类管理费预算占用单）

	public static final String BILLNAME_01 = "费用申请单"; // 费用申请单（合同类管理费预算占用单）

	public static final String BILL_02 = "02"; // 进项发票

	public static final String BILLNAME_02 = "进项发票"; // 进项发票

	public static final String BILL_03 = "03"; // 应付单

	public static final String BILLNAME_03 = "应付单"; // 应付单

	public static final String BILL_04 = "04"; // 付款申请单->付款单

	public static final String BILLNAME_04 = "付款申请单"; // 付款申请单->付款单

	public static final String BILL_05 = "05"; // 付款合同

	public static final String BILLNAME_05 = "付款合同"; // 付款合同

	public static final String BILL_06 = "06"; // 收款合同

	public static final String BILLNAME_06 = "收款合同"; // 收款合同

	public static final String BILL_07 = "07"; // 保证金工单

	public static final String BILLNAME_07 = "保证金工单"; // 保证金工单

	public static final String BILL_08 = "08"; // 土地分摊工单

	public static final String BILLNAME_08 = "土地分摊工单"; // 土地分摊工单

	public static final String BILL_09 = "09"; // 土地利息、契税工单

	public static final String BILLNAME_09 = "土地利息、契税工单"; // 土地利息、契税工单

	// public static final String BILL_10 = "10"; // 暂估应付工单
	//
	// public static final String BILLNAME_10 = "暂估应付工单"; // 暂估应付工单

	public static final String BILL_10 = "10"; // 到货单
	public static final String BILLNAME_10 = "到货明细表,暂估应付工单"; // 到货单

	public static final String BILL_11 = "11"; // 对账单

	public static final String BILLNAME_11 = "对账单"; // 对账单

	public static final String BILL_12 = "12"; // 应收单

	public static final String BILLNAME_12 = "应收单"; // 应收单

	public static final String BILL_13 = "13"; // 出库工单

	public static final String BILLNAME_13 = "出库工单"; // 出库工单

	public static final String Bill_14 = "14";// 支出类合同

	public static final String BILLNAME_14 = "支出类合同"; // 支出类合同

	public static final String BILL_15 = "15";// 销项发票开票

	public static final String BILLNAME_15 = "销项发票"; // 销项发票开票

	public static final String BILL_16 = "16";// 保证金工单

	public static final String BILLNAME_16 = "保证金工单"; // 保证金工单

	public static final String BILL_17 = "17";// 付款合同-成本类合同

	public static final String BILLNAME_17 = "付款合同-成本类合同"; // 付款合同-成本类合同

	public static final String BILL_18 = "18";// 付款合同-采购材料合同（ebs采购协议单）

	public static final String BILLNAME_18 = "付款合同-采购材料合同（ebs采购协议单）"; // 付款合同-采购材料合同（ebs采购协议单）

	public static final String BILL_19 = "19";// 出库单-材料应收单

	public static final String BILLNAME_19 = "SRM-出库单-->应收单"; // 出库单-材料应收单

	public static final String BILL_20 = "20";// SRM-供应链收供应商招投标保证金

	public static final String BILLNAME_20 = "SRM-供应链收供应商招投标保证金"; // SRM-供应链收供应商招投标保证金

	public static final String BILL_25 = "25";// EBS通用-应付与申请

	public static final String BILLNAME_25 = "EBS通用-应付与申请";

	public static final String BILL_21 = "21";// 通用报销单

	public static final String BILLNAME_21 = "通用报销单"; // 通用报销单

	public static final String BILL_22 = "22";// SRM投标保证金应收单

	public static final String BILLNAME_22 = "SRM-投标保证金应收单"; // SRM投标保证金应收单

	public static final String BILL_23 = "23";// EBS成本-应付单与付款申请单

	public static final String BILLNAME_23 = "EBS成本-应付单与付款申请单"; // EBS成本-应付单与付款申请单
	public static final String BILL_26 = "26";// 批量销项发票

	public static final String BILLNAME_26 = "批量销项发票";
	/**
	 * 
	 */
	public static final String BILL_27 = "27";// srm供应链对供应商对账信息传nc【开票工单】

	public static final String BILLNAME_27 = "SRM供应链对供应商对账信息【开票工单】";
	// 成本税差应付单-2020-05-25-谈子健
	public static final String BILL_28 = "28";// 成本税差应付单-2020-05-25-谈子健
	public static final String BILLNAME_28 = "EBS->成本税差应付单";
	// 成本占预算应付单-2020-05-26-谈子健
	public static final String BILL_29 = "29";
	public static final String BILLNAME_29 = "EBS->成本占预算应付单";
	// EBS成本-应付单与付款申请单-2020-05-30-谈子健
	public static final String BILL_30 = "30";
	public static final String BILLNAME_30 = "EBS成本-应付单与付款申请单";
	// 通用税差应付单-2020-06-08-谈子健
	public static final String BILL_31 = "31";
	public static final String BILLNAME_31 = "EBS->通用税差应付单";
	// 通用占预算应付单-2020-05-26-谈子健
	public static final String BILL_32 = "32";
	public static final String BILLNAME_32 = "EBS->通用占预算应付单";

	/**
	 * 目标业务单据中文名对照 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILL_01, BILLNAME_01);
			billNameMap.put(BILL_02, BILLNAME_02);
			billNameMap.put(BILL_03, BILLNAME_03);
			billNameMap.put(BILL_04, BILLNAME_04);
			billNameMap.put(BILL_05, BILLNAME_05);
			billNameMap.put(BILL_06, BILLNAME_06);
			billNameMap.put(BILL_07, BILLNAME_07);
			billNameMap.put(BILL_08, BILLNAME_08);
			billNameMap.put(BILL_09, BILLNAME_09);
			billNameMap.put(BILL_10, BILLNAME_10);
			billNameMap.put(BILL_11, BILLNAME_11);
			billNameMap.put(BILL_12, BILLNAME_12);
			billNameMap.put(BILL_13, BILLNAME_13);
			billNameMap.put(BILL_15, BILLNAME_15);
			billNameMap.put(BILL_16, BILLNAME_16);
			billNameMap.put(BILL_17, BILLNAME_17);
			billNameMap.put(BILL_18, BILLNAME_18);
			billNameMap.put(BILL_19, BILLNAME_19);
			billNameMap.put(BILL_20, BILLNAME_20);
			billNameMap.put(BILL_21, BILLNAME_21);
			billNameMap.put(BILL_22, BILLNAME_22);
			billNameMap.put(BILL_25, BILLNAME_25);
			billNameMap.put(BILL_26, BILLNAME_26);
			billNameMap.put(BILL_27, BILLNAME_27);
			billNameMap.put(BILL_28, BILLNAME_28);
			billNameMap.put(BILL_29, BILLNAME_29);
			billNameMap.put(BILL_30, BILLNAME_30);
			billNameMap.put(BILL_31, BILLNAME_31);
			billNameMap.put(BILL_32, BILLNAME_32);
		}
		return billNameMap;
	}

	// 来源交易类型指向NC业务单
	private static Map<String, String> srcBillToDesBillMap = null;
	private static Map<String, String> srcBillNameMap = null;

	public static final String SRCBILL_01 = "01"; // EBS-通用合同->费用预提单
	public static final String SRCBILLNAME_01 = "EBS-合同预占单->费用预占单";
	public static final String SRCBILL_02 = "02"; // EBS-合同预提单->费用预提单
	public static final String SRCBILLNAME_02 = "EBS-预算预提单->费用预提单"; //
	public static final String SRCBILL_03 = "03"; // EBS-通用合同请款->应付单
	public static final String SRCBILLNAME_03 = "EBS-通用合同请款->应付单"; // EBS-通用合同请款->应付单
	public static final String SRCBILL_04 = "04"; // EBS-通用合同请款->付款申请单(费用)
	public static final String SRCBILLNAME_04 = "EBS-通用合同请款->付款申请单";
	public static final String SRCBILL_05 = "05"; // EBS-营销费请款->应付单
	public static final String SRCBILLNAME_05 = "EBS-营销费请款->应付单";
	public static final String SRCBILL_06 = "06"; // EBS-营销费请款->付款申请单 (费用)
	public static final String SRCBILLNAME_06 = "EBS-营销费请款->付款申请单";
	public static final String SRCBILL_07 = "07"; // EBS-项目公司佣金请款->付款申请单 (费用)
	public static final String SRCBILLNAME_07 = "EBS-项目公司佣金请款->付款申请单";
	public static final String SRCBILL_08 = "08"; // EBS通用支出合同->付款合同
	public static final String SRCBILLNAME_08 = "EBS通用支出合同->付款合同";
	public static final String SRCBILL_09 = "09"; // SRM对账单->应付单
	public static final String SRCBILLNAME_09 = "SRM对账单->应付单";
	public static final String SRCBILL_10 = "10"; // EBS-供应商请款单->付款申请单 (成本-保证金)
	public static final String SRCBILLNAME_10 = "EBS-供应商请款单->付款申请单";
	public static final String SRCBILL_11 = "11"; // EBS-合同变更->保证金工单
	public static final String SRCBILLNAME_11 = "EBS-合同变更->保证金工单";
	public static final String SRCBILL_12 = "12"; // EBS-合同并更->土地款分摊工单
	public static final String SRCBILLNAME_12 = "EBS-合同并更->土地款分摊工单";
	public static final String SRCBILL_13 = "13"; // EBS-合同->土地利息、契税工单
	public static final String SRCBILLNAME_13 = "EBS-合同->土地利息、契税工单";

	public static final String SRCBILL_14 = "14"; // EBS-项目周建请款->应付单
	public static final String SRCBILLNAME_14 = "EBS-成本请款->应付单";

	public static final String SRCBILL_15 = "15"; // EBS-项目周建请款->付款申请单 (成本)
	public static final String SRCBILLNAME_15 = "EBS-项目周建请款->付款申请单";

	// public static final String SRCBILL_16 = "16"; // EBS-发票单据->成本零元应付单
	// public static final String SRCBILLNAME_16 = "EBS-发票单据->成本零元应付单"; //
	// EBS-发票单据->成本零元应付单

	public static final String SRCBILL_18 = "18"; // EBS-供应合同->收款合同
	public static final String SRCBILLNAME_18 = "EBS-供应合同->收款合同";

	public static final String SRCBILL_20 = "20"; // SRM-到货单->到货单
	public static final String SRCBILLNAME_20 = "SRM-到货单->到货单";

	public static final String SRCBILL_21 = "21"; // SRM-对账单->对账单
	public static final String SRCBILLNAME_21 = "SRM-对账单->对账单";

	public static final String SRCBILL_22 = "22"; // EBS-材料请款单->应付单
	public static final String SRCBILLNAME_22 = "EBS-材料请款单->应付单";

	public static final String SRCBILL_23 = "23"; // SRM-出库单->应收单
	public static final String SRCBILLNAME_23 = "SRM-出库单->应收单";

	public static final String SRCBILL_24 = "24"; // SRM-入库单->应付单
	public static final String SRCBILLNAME_24 = "SRM-入库单->应付单";

	public static final String SRCBILL_25 = "25"; // SRM-领用出库单->出库工单
	public static final String SRCBILLNAME_25 = "SRM-领用/销售出库单->出库工单";

	public static final String SRCBILL_26 = "26"; // SRM-进项发票->进项发票工单
	public static final String SRCBILLNAME_26 = "SRM-进项发票->进项发票工单";

	public static final String SRCBILL_27 = "27"; // EBS-材料请款单->付款申请单(成本-内部交易)
	public static final String SRCBILLNAME_27 = "EBS-材料请款单->付款申请单";

	public static final String SRCBILL_28 = "28"; // EBS-成本/营销类合同->付款合同
	public static final String SRCBILLNAME_28 = "EBS-成本/营销类合同->付款合同";

	public static final String SRCBILL_29 = "29"; // EBS-支出通用合同->付款合同
	public static final String SRCBILLNAME_29 = "EBS-支出通用合同->付款合同";
	public static final String SRCBILL_30 = "30"; // EBS-收入合同(地产)->收款合同
	public static final String SRCBILLNAME_30 = "EBS-收入合同(地产)->收款合同";
	public static final String SRCBILL_31 = "31"; // EBS-收入通用合同->收款合同
	public static final String SRCBILLNAME_31 = "EBS-收入通用合同->收款合同";
	public static final String SRCBILL_32 = "32"; // EBS合同变更保证金合同->保证金工单
	public static final String SRCBILLNAME_32 = "EBS合同变更保证金合同->保证金工单";

	public static final String SRCBILL_33 = "33"; // EBS
	public static final String SRCBILL_34 = "34"; // EBS
	public static final String SRCBILL_35 = "35"; // EBS
	public static final String SRCBILL_36 = "36"; // EBS

	public static final String SRCBILL_37 = "37"; // SRM-到货单->到货明细单，暂估应付工单
	public static final String SRCBILLNAME_37 = "SRM-到货单->到货明细单，暂估应付工单";

	public static final String SRCBILL_38 = "38"; // 成本/营销合同->付款合同-成本合同
	public static final String SRCBILLNAME_38 = "EBS-成本/营销合同->付款合同-成本合同";

	public static final String SRCBILL_39 = "39"; // EBS-采购协议单->付款合同-采购材料合同
	public static final String SRCBILLNAME_39 = "EBS-采购协议单->付款合同-材料合同";

	public static final String SRCBILL_40 = "40"; // 销项票开票
	public static final String SRCBILLNAME_40 = "销项票开票";

	public static final String SRCBILL_41 = "41"; // EBS-通用收并购请款->应付单
	public static final String SRCBILLNAME_41 = "EBS-通用收并购请款->应付单";

	public static final String SRCBILL_42 = "42"; // SRM-供应链收供应商招投标保证金
	public static final String SRCBILLNAME_42 = "SRM-供应链收供应商招投标保证金";
	public static final String SRCBILL_43 = "43";// 应付单挂起后动作
	public static final String SRCBILLNAME_43 = "应付单挂起后动作";
	public static final String SRCBILL_45 = "45";
	public static final String SRCBILLNAME_45 = "EBS->应付单付款单";

	public static final String SRCBILL_44 = "44";// EBS预提->报销单表体预提核销
	public static final String SRCBILLNAME_44 = "EBS预提->报销单表体预提核销";

	public static final String SRCBILL_46 = "46"; // 批量销项票开票
	public static final String SRCBILLNAME_46 = "批量销项票开票";

	public static final String SRCBILL_47 = "47"; // SRM投标保证金应收单
	public static final String SRCBILLNAME_47 = "SRM投标保证金应收单";
	/**
	 * srm供应链对供应商对账信息传nc【开票工单】 2020-04-02-谈子健
	 */
	public static final String SRCBILL_48 = "48"; // SRM供应链对供应商对账信息【开票工单】
	public static final String SRCBILLNAME_48 = "SRM供应链对供应商对账信息【开票工单】";
	// 成本税差应付单-2020-05-25-谈子健
	public static final String SRCBILL_49 = "49";
	public static final String SRCBILLNAME_49 = "EBS->成本税差应付单";
	// 成本占预算应付单
	public static final String SRCBILL_50 = "50";
	public static final String SRCBILLNAME_50 = "EBS->成本占预算应付单";
	// 成本应付单与付款申请单合并接口-2020-05-30-谈子健
	public static final String SRCBILL_51 = "51";
	public static final String SRCBILLNAME_51 = "EBS->成本应付单与付款申请单";

	// 通用税差应付单-2020-06-08-谈子健
	public static final String SRCBILL_52 = "52";
	public static final String SRCBILLNAME_52 = "EBS->通用税差应付单";
	// 通用占预算应付单-2020-06-08-谈子健
	public static final String SRCBILL_53 = "53";
	public static final String SRCBILLNAME_53 = "EBS->通用占预算应付单";

	public static Map<String, String> getSrcBillToDesBillMap() {
		if (srcBillToDesBillMap == null) {
			srcBillToDesBillMap = new HashMap<String, String>();
			srcBillToDesBillMap.put(SRCBILL_01, BILL_01);// EBS-通用合同->费用预提单
			srcBillToDesBillMap.put(SRCBILL_02, BILL_01);// EBS-合同预提单->费用预提单
			srcBillToDesBillMap.put(SRCBILL_03, BILL_03);// EBS-通用合同请款->应付单
			srcBillToDesBillMap.put(SRCBILL_04, BILL_04);// EBS-通用合同请款->付款申请单
			srcBillToDesBillMap.put(SRCBILL_05, BILL_03);// EBS-营销费请款->应付单
			srcBillToDesBillMap.put(SRCBILL_06, BILL_04);// EBS-营销费请款->付款申请单
			srcBillToDesBillMap.put(SRCBILL_07, BILL_04);// EBS-项目公司佣金请款->付款申请单
			srcBillToDesBillMap.put(SRCBILL_08, BILL_05);// EBS-管理类非合同/成本合同->付款合同

			srcBillToDesBillMap.put(SRCBILL_09, BILL_03);// SRM对账单->应付单
			srcBillToDesBillMap.put(SRCBILL_10, BILL_04);// EBS-供应商请款单->付款申请单
			srcBillToDesBillMap.put(SRCBILL_11, BILL_07);// EBS-E合同变更->保证金工单
			srcBillToDesBillMap.put(SRCBILL_12, BILL_08);// EBS-合同并更->土地款分摊工单
			srcBillToDesBillMap.put(SRCBILL_13, BILL_09);// EBS-合同->土地利息、契税工单
			srcBillToDesBillMap.put(SRCBILL_14, BILL_03);// EBS-项目周建请款->应付单
			srcBillToDesBillMap.put(SRCBILL_15, BILL_04);// EBS-项目周建请款->付款申请单
			// srcBillToDesBillMap.put(SRCBILL_16, BILL_03);// EBS-发票单据->成本零元应付单
			// srcBillToDesBillMap.put(SRCBILL_17, BILL_05);// SRM-采购合同->付款合同
			srcBillToDesBillMap.put(SRCBILL_18, BILL_06);// EBS-供应合同->收款合同
			// srcBillToDesBillMap.put(SRCBILL_19, BILL_05);// EBS-供应合同->付款合同
			srcBillToDesBillMap.put(SRCBILL_20, BILL_10);// SRM-到货单->到货单
			srcBillToDesBillMap.put(SRCBILL_21, BILL_11);// SRM-对账单->对账单
			srcBillToDesBillMap.put(SRCBILL_22, BILL_03);// EBS-材料请款单->应付单
			srcBillToDesBillMap.put(SRCBILL_23, BILL_19);// SRM-出库单->应收单
			srcBillToDesBillMap.put(SRCBILL_24, BILL_03);// SRM-入库单->应付单
			srcBillToDesBillMap.put(SRCBILL_25, BILL_13);// SRM-领用出库单->出库工单
			srcBillToDesBillMap.put(SRCBILL_26, BILL_02);// SRM-进项发票->进项发票工单
			srcBillToDesBillMap.put(SRCBILL_27, BILL_04);// SEBS-材料请款单->付款申请单

			srcBillToDesBillMap.put(SRCBILL_28, BILL_04);// EBS-成本/营销类合同->付款合同
			srcBillToDesBillMap.put(SRCBILL_29, BILL_04);// EBS-支出通用合同->付款合同
			srcBillToDesBillMap.put(SRCBILL_30, BILL_05);// EBS-收入合同(地产)->收款合同
			srcBillToDesBillMap.put(SRCBILL_31, BILL_06);// EBS-收入通用合同->收款合同
			srcBillToDesBillMap.put(SRCBILL_32, BILL_16);// EBS合同变更保证金合同->保证金工单

			srcBillToDesBillMap.put(SRCBILL_37, BILL_10);// SRM-到货单->到货明细单，暂估应付工单
			srcBillToDesBillMap.put(SRCBILL_40, BILL_15);// 销项发票
			srcBillToDesBillMap.put(SRCBILL_38, BILL_05);// EBS-管理类非合同/成本合同->付款合同-成本合同
			srcBillToDesBillMap.put(SRCBILL_39, BILL_05);// EBS-采购协议单->付款合同-采购材料合同
			srcBillToDesBillMap.put(SRCBILL_41, BILL_03);// EBS-通用收并购请款->应付单
			srcBillToDesBillMap.put(SRCBILL_42, BILL_20);// SRM-供应链收供应商招投标保证金
			srcBillToDesBillMap.put(SRCBILL_43, BILL_03);// 成本应付单挂起后动作

			srcBillToDesBillMap.put(SRCBILL_44, BILL_21);// EBS预提->报销单表体预提核销
			srcBillToDesBillMap.put(SRCBILL_45, BILL_25);// EBS通用->应付和付款申请单

			srcBillToDesBillMap.put(SRCBILL_46, BILL_26);// 销售系统批量销项发票
			srcBillToDesBillMap.put(SRCBILL_47, BILL_22);// SRM投标保证金应收单
			/**
			 * srm供应链对供应商对账信息传nc【开票工单】 2020-04-02-谈子健
			 */
			srcBillToDesBillMap.put(SRCBILL_48, BILL_27);
			// 成本税差应付单-2020-05-25-谈子健
			srcBillToDesBillMap.put(SRCBILL_49, BILL_28);
			// EBS->成本占预算应付单-2020-05-26
			srcBillToDesBillMap.put(SRCBILL_50, BILL_29);
			// EBS->成本应付单与付款申请单-2020-05-30-谈子健
			srcBillToDesBillMap.put(SRCBILL_51, BILL_30);
			// EBS->通用税差应付单-2020-06-08-谈子健
			srcBillToDesBillMap.put(SRCBILL_52, BILL_31);
			// EBS->通用占预算应付单-2020-06-08-谈子健
			srcBillToDesBillMap.put(SRCBILL_53, BILL_32);
		}
		return srcBillToDesBillMap;
	}

	public static Map<String, String> getSrcBillNameMap() {
		if (srcBillNameMap == null) {
			srcBillNameMap = new HashMap<String, String>();
			srcBillNameMap.put(SRCBILL_01, SRCBILLNAME_01);
			srcBillNameMap.put(SRCBILL_02, SRCBILLNAME_02);
			srcBillNameMap.put(SRCBILL_03, SRCBILLNAME_03);
			srcBillNameMap.put(SRCBILL_04, SRCBILLNAME_04);
			srcBillNameMap.put(SRCBILL_05, SRCBILLNAME_05);
			srcBillNameMap.put(SRCBILL_06, SRCBILLNAME_06);
			srcBillNameMap.put(SRCBILL_07, SRCBILLNAME_07);
			srcBillNameMap.put(SRCBILL_08, SRCBILLNAME_08);
			srcBillNameMap.put(SRCBILL_09, SRCBILLNAME_09);
			srcBillNameMap.put(SRCBILL_10, SRCBILLNAME_10);
			srcBillNameMap.put(SRCBILL_11, SRCBILLNAME_11);
			srcBillNameMap.put(SRCBILL_12, SRCBILLNAME_12);
			srcBillNameMap.put(SRCBILL_13, SRCBILLNAME_13);
			srcBillNameMap.put(SRCBILL_14, SRCBILLNAME_14);
			srcBillNameMap.put(SRCBILL_15, SRCBILLNAME_15);
			// srcBillNameMap.put(SRCBILL_16, SRCBILLNAME_16);
			// srcBillNameMap.put(SRCBILL_17, SRCBILLNAME_17);
			srcBillNameMap.put(SRCBILL_18, SRCBILLNAME_18);
			// srcBillNameMap.put(SRCBILL_19, SRCBILLNAME_19);
			srcBillNameMap.put(SRCBILL_20, SRCBILLNAME_20);
			srcBillNameMap.put(SRCBILL_21, SRCBILLNAME_21);
			srcBillNameMap.put(SRCBILL_22, SRCBILLNAME_22);
			srcBillNameMap.put(SRCBILL_23, SRCBILLNAME_23);
			srcBillNameMap.put(SRCBILL_24, SRCBILLNAME_24);
			srcBillNameMap.put(SRCBILL_25, SRCBILLNAME_25);
			srcBillNameMap.put(SRCBILL_26, SRCBILLNAME_26);
			srcBillNameMap.put(SRCBILL_27, SRCBILLNAME_27);
			srcBillNameMap.put(SRCBILL_28, SRCBILLNAME_28);
			srcBillNameMap.put(SRCBILL_29, SRCBILLNAME_29);
			srcBillNameMap.put(SRCBILL_30, SRCBILLNAME_30);
			srcBillNameMap.put(SRCBILL_31, SRCBILLNAME_31);
			srcBillNameMap.put(SRCBILL_37, SRCBILLNAME_37);
			// srcBillNameMap.put(BILL_15, BILLNAME_15);
			srcBillNameMap.put(SRCBILL_38, SRCBILLNAME_38);
			srcBillNameMap.put(SRCBILL_39, SRCBILLNAME_39);
			srcBillNameMap.put(SRCBILL_32, SRCBILLNAME_32);
			srcBillNameMap.put(SRCBILL_40, SRCBILLNAME_40);

			srcBillNameMap.put(SRCBILL_41, SRCBILLNAME_41);
			srcBillNameMap.put(SRCBILL_43, SRCBILLNAME_43);

			srcBillNameMap.put(SRCBILL_44, SRCBILLNAME_44);

			srcBillNameMap.put(SRCBILL_45, SRCBILLNAME_45);

			srcBillNameMap.put(SRCBILL_46, SRCBILLNAME_46);
			srcBillNameMap.put(SRCBILL_47, SRCBILLNAME_47);
			srcBillNameMap.put(SRCBILL_48, SRCBILLNAME_48);
			srcBillNameMap.put(SRCBILL_49, SRCBILLNAME_49);
			srcBillNameMap.put(SRCBILL_50, SRCBILLNAME_50);
			srcBillNameMap.put(SRCBILL_51, SRCBILLNAME_51);
			srcBillNameMap.put(SRCBILL_52, SRCBILLNAME_52);
			srcBillNameMap.put(SRCBILL_53, SRCBILLNAME_53);
		}
		return srcBillNameMap;
	}

}
