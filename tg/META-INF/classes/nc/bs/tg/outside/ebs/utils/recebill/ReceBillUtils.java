package nc.bs.tg.outside.ebs.utils.recebill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.arap.bill.ArapBillCalUtil;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ArapDataDataSet;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.fi.pub.SysInit;
import nc.itf.tg.outside.EBSCont;
import nc.pubitf.bbd.CurrtypeQuery;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.arap.basebill.BaseItemVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.Direction;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.arap.sysinit.SysinitConst;
import nc.vo.arappub.calculator.data.RelationItemForCal_Debit;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.pattern.pub.MathTool;
import nc.vo.pubapp.scale.ScaleUtils;
import nc.vo.tg.recebill.ReceivableBodyVO;
import nc.vo.tg.recebill.ReceivableHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ReceBillUtils extends EBSBillUtils {
	static ReceBillUtils utils;
	private IErmAccruedBillManage billManagerService;

	public static ReceBillUtils getUtils() {
		if (utils == null) {
			utils = new ReceBillUtils();
		}
		return utils;
	}

	/**
	 * 应收单
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
		// 处理表单信息
		JSONObject jsonData = (JSONObject) value.get("data");// 表单数据
		String jsonhead = jsonData.getString("receivableHeadVO");// 外系统来源表头数据
		String jsonbody = jsonData.getString("receivableBodyVOs");// 外系统来源表体数据
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		// 转换json
		ReceivableHeadVO headVO = JSONObject.parseObject(jsonhead,
				ReceivableHeadVO.class);
		List<ReceivableBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				ReceivableBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}

		String srcid = headVO.getSrcsid();// 外系统业务单据ID
		String srcno = headVO.getSrcbillcode();// 外系统业务单据单据号
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		AggReceivableBillVO[] aggvo=null;
		// // TODO ebsid 按实际存入信息位置进行变更
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "isnull(dr,0)=0 and def1 = '"
						+ srcid + "'");
		if (aggVO != null) {
			throw new BusinessException("【"
					+ billkey
					+ "】,NC已存在对应的业务单据【"
					+ aggVO.getParentVO().getAttributeValue(
							ReceivableBillVO.BILLNO) + "】,请勿重复上传!");
		}
		EBSBillUtils.addBillQueue(billqueue);// 增加队列处理
		try {
			AggReceivableBillVO billvo = onTranBill(headVO, bodyVOs, srctype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo=(AggReceivableBillVO[]) getPfBusiAction().processAction("SAVE", "F0", null, billvo, null,
					eParam);
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo[0].getPrimaryKey());
		dataMap.put("billno", (String) aggvo[0].getParentVO()
				.getAttributeValue(ReceivableBillVO.BILLNO));
		return JSON.toJSONString(dataMap);

	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	private AggReceivableBillVO onTranBill(ReceivableHeadVO headvo,
			List<ReceivableBodyVO> bodyVOs, String srctype) throws Exception {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		save_headVO.setAttributeValue("pk_org",
				getPk_orgByCode(headvo.getOrg()));// 应收财务组织
		save_headVO.setAttributeValue("pk_fiorg", null);// 废弃财务组织
		save_headVO.setAttributeValue("pk_pcorg", null);// 利润中心
		save_headVO.setAttributeValue("sett_org", null);// 结算财务组织
		save_headVO.setAttributeValue("pk_org_v", null);// 应收财务组织版本
		save_headVO.setAttributeValue("pk_fiorg_v", null);// 废弃财务组织版本
		save_headVO.setAttributeValue("pk_pcorg_v", null);// 利润中心版本
		save_headVO.setAttributeValue("sett_org_v", null);// 结算财务组织版本
		save_headVO.setAttributeValue("isreded", UFBoolean.FALSE);// 是否红冲过
		save_headVO.setAttributeValue("outbusitype", null);// 外系统业务类型
		save_headVO.setAttributeValue("officialprintuser", null);// 正式打印人
		save_headVO.setAttributeValue("officialprintdate", null);// 正式打印日期
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
		save_headVO.setAttributeValue("modifiedtime", null);// 最后修改时间
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(headvo.getCreator()));// 创建人
		save_headVO.setAttributeValue("pk_billtype", "F0");// 单据类型编码
		save_headVO.setAttributeValue("custdelegate", null);// 代垫单位
		save_headVO.setAttributeValue("pk_corp", null);// 单位编码
		save_headVO.setAttributeValue("modifier", null);// 最后修改人
		save_headVO.setAttributeValue("pk_tradetype", "D0");// 应收类型code
		save_headVO.setAttributeValue("pk_tradetypeid", "0001ZZ10000000258KGQ");// 应收类型
		save_headVO.setAttributeValue("billclass", "ys");// 单据大类
		save_headVO.setAttributeValue("pk_recbill", null);// 应收单标识
		save_headVO.setAttributeValue("accessorynum", null);// 附件张数
		save_headVO.setAttributeValue("subjcode", null);// 科目编码
		save_headVO.setAttributeValue("isflowbill", UFBoolean.FALSE);// 是否流程单据
		save_headVO.setAttributeValue("confirmuser", null);// 单据确认人
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// 期初标志
		save_headVO.setAttributeValue("billno", null);// 单据号
		save_headVO.setAttributeValue("billdate", new UFDate());// 单据日期
		save_headVO.setAttributeValue("syscode", 0);// 单据所属系统
		save_headVO.setAttributeValue("src_syscode", 0);// 单据来源系统
		save_headVO.setAttributeValue("billstatus", -1);// 单据状态
		save_headVO.setAttributeValue("billmaker",
				getUserPkByCode(headvo.getCreator()));// 制单人
		save_headVO.setAttributeValue("approver", null);// 审核人
		save_headVO.setAttributeValue("approvedate", null);// 审核时间
		save_headVO.setAttributeValue("lastadjustuser", null);// 最终调整人
		save_headVO.setAttributeValue("signuser", null);// 签字人
		save_headVO.setAttributeValue("signyear", null);// 签字年度
		save_headVO.setAttributeValue("signperiod", null);// 签字期间
		save_headVO.setAttributeValue("signdate", null);// 签字日期
		save_headVO.setAttributeValue("pk_busitype", "0001ZZ10000000258BF2");// 业务流程
		save_headVO.setAttributeValue("money", null);// 原币金额
		save_headVO.setAttributeValue("local_money", null);// 组织本币金额
		save_headVO.setAttributeValue("billyear", null);// 单据会计年度
		save_headVO.setAttributeValue("billperiod", null);// 单据会计期间
		save_headVO.setAttributeValue("scomment", null);// 摘要
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("effectuser", null);// 生效人
		save_headVO.setAttributeValue("effectdate", null);// 生效日期
		save_headVO.setAttributeValue("lastapproveid", null);// 最终审批人
		save_headVO.setAttributeValue("def1", headvo.getContractcode());// 自定义项1
		save_headVO.setAttributeValue("def30", null);// 自定义项30
		save_headVO.setAttributeValue("def29", null);// 自定义项29
		save_headVO.setAttributeValue("def28", null);// 自定义项28
		save_headVO.setAttributeValue("def27", null);// 自定义项27
		save_headVO.setAttributeValue("def26", null);// 自定义项26
		save_headVO.setAttributeValue("def25", null);// 自定义项25
		save_headVO.setAttributeValue("def24", null);// 自定义项24
		save_headVO.setAttributeValue("def23", null);// 自定义项23
		save_headVO.setAttributeValue("def22", null);// 自定义项22
		save_headVO.setAttributeValue("def21", null);// 自定义项21
		save_headVO.setAttributeValue("def20", null);// 自定义项20
		save_headVO.setAttributeValue("def19", null);// 自定义项19
		save_headVO.setAttributeValue("def18", null);// 自定义项18
		save_headVO.setAttributeValue("def17", null);// 自定义项17
		save_headVO.setAttributeValue("def16", null);// 自定义项16
		save_headVO.setAttributeValue("def15", null);// 自定义项15
		save_headVO.setAttributeValue("def14", null);// 自定义项14
		save_headVO.setAttributeValue("def13", null);// 自定义项13
		save_headVO.setAttributeValue("def12", null);// 自定义项12
		save_headVO.setAttributeValue("def11", null);// 自定义项11
		save_headVO.setAttributeValue("def10", null);// 自定义项10
		save_headVO.setAttributeValue("def9", null);// 自定义项9
		save_headVO.setAttributeValue("def8", null);// 自定义项8
		save_headVO.setAttributeValue("def7", null);// 自定义项7
		save_headVO.setAttributeValue("def6", null);// 自定义项6
		save_headVO.setAttributeValue("def5", null);// 自定义项5
		save_headVO.setAttributeValue("def4", null);// 自定义项4
		save_headVO.setAttributeValue("def3", null);// 自定义项3
		save_headVO.setAttributeValue("def2", headvo.getTotalamount());// 应收款金额
		save_headVO.setAttributeValue("grouplocal", null);// 集团本币金额
		save_headVO.setAttributeValue("globallocal", null);// 全局本币金额
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种
		save_headVO.setAttributeValue("coordflag", null);// 单据协同标志
		save_headVO.setAttributeValue("inner_effect_date", null);// 内控账期日期
		save_headVO.setAttributeValue("approvestatus", 3);// 审批状态
		save_headVO.setAttributeValue("sendcountryid", null);// 发货国
		save_headVO.setAttributeValue("taxcountryid", null);// 报税国
		save_headVO.setStatus(VOStatus.NEW);

		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// 应收财务组织
			save_bodyVO.setAttributeValue("pk_pcorg", null);// 利润中心
			save_bodyVO.setAttributeValue("pk_fiorg", null);// 废弃财务组织
			save_bodyVO.setAttributeValue("sett_org", null);// 结算财务组织
			save_bodyVO.setAttributeValue("pk_pcorg_v", null);// 利润中心版本
			save_bodyVO.setAttributeValue("pk_org_v", null);// 应收财务组织版本
			save_bodyVO.setAttributeValue("pk_fiorg_v", null);// 废弃财务组织版本
			save_bodyVO.setAttributeValue("sett_org_v", null);// 结算财务组织版本
			save_bodyVO.setAttributeValue("so_org_v", null);// 业务组织版本
			save_bodyVO.setAttributeValue("so_deptid", null);// 业务部门
			save_bodyVO.setAttributeValue("so_deptid_v", null);// 业务部门版本
			save_bodyVO.setAttributeValue("so_psndoc", null);// 业务人员
			save_bodyVO.setAttributeValue("so_ordertype", null);// 销售订单类型
			save_bodyVO.setAttributeValue("so_transtype", null);// 销售渠道类型
			save_bodyVO.setAttributeValue("so_org", null);// 业务组织
			save_bodyVO.setAttributeValue("material", null);// 物料
			save_bodyVO.setAttributeValue("customer",
					getcustomer(receivableBodyVO.getCustomer()));// 客户
			save_bodyVO.setAttributeValue("postunit", null);// 报价计量单位
			save_bodyVO.setAttributeValue("postpricenotax", null);// 报价单位无税单价
			save_bodyVO.setAttributeValue("postquantity", null);// 报价单位数量
			save_bodyVO.setAttributeValue("postprice", null);// 报价单位含税单价
			save_bodyVO.setAttributeValue("coordflag", null);// 单据协同状态
			save_bodyVO.setAttributeValue("equipmentcode", null);// 设备编码
			save_bodyVO.setAttributeValue("productline", null);// 产品线
			save_bodyVO.setAttributeValue("cashitem", null);// 现金流量项目
			save_bodyVO.setAttributeValue("bankrollprojet", null);// 资金计划项目
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// 挂起标志
			save_bodyVO
					.setAttributeValue("billdate", save_headVO.getBilldate());// 单据日期
			save_bodyVO
					.setAttributeValue("pk_group", save_headVO.getPk_group());// 所属集团
			save_bodyVO.setAttributeValue("pk_billtype",
					save_headVO.getPk_billtype());// 单据类型编码
			save_bodyVO.setAttributeValue("billclass",
					save_headVO.getBillclass());// 单据大类
			save_bodyVO.setAttributeValue("pk_tradetype",
					save_headVO.getPk_tradetype());// 应收类型code
			save_bodyVO.setAttributeValue("pk_tradetypeid",
					save_headVO.getPk_tradetypeid());// 应收类型
			save_bodyVO.setAttributeValue("pk_recitem", null);// 应收单行标识
			save_bodyVO
					.setAttributeValue("busidate", save_headVO.getBilldate());// 起算日期
			save_bodyVO.setAttributeValue("pk_subjcode", null);// 收支项目
			save_bodyVO.setAttributeValue("billno", null);// 单据编号
			save_bodyVO.setAttributeValue("objtype", 0);// 往来对象
			save_bodyVO.setAttributeValue("rowno", null);// 单据分录号
			save_bodyVO.setAttributeValue("rowtype", null);// 行类型
			save_bodyVO.setAttributeValue("direction", 1);// 方向
			save_bodyVO.setAttributeValue("pk_ssitem", null);// 事项审批单
			save_bodyVO.setAttributeValue("scomment", null);// 摘要
			save_bodyVO.setAttributeValue("subjcode", null);// 科目编码
			save_bodyVO.setAttributeValue("pk_currtype",
					save_headVO.getPk_currtype());// 币种
			save_bodyVO.setAttributeValue("rate", 1);// 组织本币汇率
			save_bodyVO.setAttributeValue("pk_deptid", null);// 部门
			save_bodyVO.setAttributeValue("pk_deptid_v", null);// 部 门
			save_bodyVO.setAttributeValue("pk_psndoc", null);// 业务员
			save_bodyVO.setAttributeValue("money_de", new UFDouble(
					receivableBodyVO.getMoney_de()));// 借方原币金额
			save_bodyVO.setAttributeValue("local_money_de", null);// 组织本币金额
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(
					receivableBodyVO.getQuantity_de()));// 借方数量
			save_bodyVO.setAttributeValue("money_bal", null);// 原币余额
			save_bodyVO.setAttributeValue("local_money_bal", null);// 组织本币余额
			save_bodyVO.setAttributeValue("quantity_bal", null);// 数量余额
			save_bodyVO.setAttributeValue("local_tax_de",
					receivableBodyVO.getLocal_tax_de());// 税额
			save_bodyVO.setAttributeValue("notax_de",
					receivableBodyVO.getNotax_de());// 借方原币无税金额
			save_bodyVO.setAttributeValue("local_notax_de", null);// 组织本币无税金额
			save_bodyVO.setAttributeValue("price", null);// 单价
			save_bodyVO.setAttributeValue("taxprice",
					receivableBodyVO.getTaxprice());// 含税单价
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(
					receivableBodyVO.getTaxrate()));// 税率
			save_bodyVO.setAttributeValue("taxnum", null);// 税号
			save_bodyVO.setAttributeValue("top_billid", null);// 上层单据主键
			save_bodyVO.setAttributeValue("top_itemid", null);// 上层单据行主键
			save_bodyVO.setAttributeValue("top_billtype", null);// 上层单据类型
			save_bodyVO.setAttributeValue("top_tradetype", null);// 上层交易类型
			save_bodyVO.setAttributeValue("src_tradetype", null);// 源头交易类型
			save_bodyVO.setAttributeValue("src_billtype", null);// 源头单据类型
			save_bodyVO.setAttributeValue("src_billid", null);// 源头单据主键
			save_bodyVO.setAttributeValue("src_itemid", null);// 源头单据行主键
			save_bodyVO.setAttributeValue("taxtype", 1);// 扣税类别
			save_bodyVO.setAttributeValue("pk_payterm", null);// 收款协议
			save_bodyVO.setAttributeValue("payaccount", null);// 付款银行账户
			save_bodyVO.setAttributeValue("recaccount", null);// 收款银行账户
			save_bodyVO.setAttributeValue("ordercubasdoc", null);// 订单客户
			save_bodyVO.setAttributeValue("innerorderno", null);// 调拨订单号
			save_bodyVO.setAttributeValue("assetpactno", null);// 资产合同号
			save_bodyVO.setAttributeValue("contractno", null);// 合同号
			save_bodyVO.setAttributeValue("freecust", null);// 散户
			save_bodyVO.setAttributeValue("purchaseorder", null);// 订单号
			save_bodyVO.setAttributeValue("invoiceno", null);// 发票号
			save_bodyVO.setAttributeValue("outstoreno", null);// 出库单号
			save_bodyVO.setAttributeValue("checkelement", null);// 责任核算要素
			save_bodyVO.setAttributeValue("def30", null);// 自定义项30
			save_bodyVO.setAttributeValue("def29", null);// 自定义项29
			save_bodyVO.setAttributeValue("def28", null);// 自定义项28
			save_bodyVO.setAttributeValue("def27", null);// 自定义项27
			save_bodyVO.setAttributeValue("def26", null);// 自定义项26
			save_bodyVO.setAttributeValue("def25", null);// 自定义项25
			save_bodyVO.setAttributeValue("def24", null);// 自定义项24
			save_bodyVO.setAttributeValue("def23", null);// 自定义项23
			save_bodyVO.setAttributeValue("def22", null);// 自定义项22
			save_bodyVO.setAttributeValue("def21", null);// 自定义项21
			save_bodyVO.setAttributeValue("def20", null);// 自定义项20
			save_bodyVO.setAttributeValue("def19", null);// 自定义项19
			save_bodyVO.setAttributeValue("def18", null);// 自定义项18
			save_bodyVO.setAttributeValue("def17", null);// 自定义项17
			save_bodyVO.setAttributeValue("def16", null);// 自定义项16
			save_bodyVO.setAttributeValue("def15", null);// 自定义项15
			save_bodyVO.setAttributeValue("def14", null);// 自定义项14
			save_bodyVO.setAttributeValue("def13", null);// 自定义项13
			save_bodyVO.setAttributeValue("def12", null);// 自定义项12
			save_bodyVO.setAttributeValue("def11", null);// 自定义项11
			save_bodyVO.setAttributeValue("def10", null);// 自定义项10
			save_bodyVO.setAttributeValue("def9", null);// 自定义项9
			save_bodyVO.setAttributeValue("def8", null);// 自定义项8
			save_bodyVO.setAttributeValue("pk_recbill", null);// 客户应收单标识
			save_bodyVO.setAttributeValue("def7", null);// 自定义项7
			save_bodyVO.setAttributeValue("def6", null);// 自定义项6
			save_bodyVO.setAttributeValue("def5", null);// 自定义项5
			save_bodyVO.setAttributeValue("def4", null);// 自定义项4
			save_bodyVO.setAttributeValue("def3", null);// 自定义项3
			save_bodyVO.setAttributeValue("def2", null);// 自定义项2
			save_bodyVO.setAttributeValue("def1",
					receivableBodyVO.getContractcode());// 供应合同编号
			save_bodyVO.setAttributeValue("grouprate", null);// 集团本币汇率
			save_bodyVO.setAttributeValue("globalrate", null);// 全局本币汇率
			save_bodyVO.setAttributeValue("groupdebit", null);// 集团本币金额(借方)
			save_bodyVO.setAttributeValue("globaldebit", null);// 全局本币金额(借方)
			save_bodyVO.setAttributeValue("groupbalance", null);// 集团本币余额
			save_bodyVO.setAttributeValue("globalbalance", null);// 全局本币余额
			save_bodyVO.setAttributeValue("groupnotax_de", null);// 集团本币无税金额(借方)
			save_bodyVO.setAttributeValue("globalnotax_de", null);// 全局本币无税金额(借方)
			save_bodyVO.setAttributeValue("occupationmny", null);// 预占用原币余额
			save_bodyVO.setAttributeValue("project", null);// 项目
			save_bodyVO.setAttributeValue("project_task", null);// 项目任务
			save_bodyVO.setAttributeValue("settleno", null);// 结算清单号
			save_bodyVO.setAttributeValue("local_price", null);// 本币单价
			save_bodyVO.setAttributeValue("local_taxprice", null);// 本币含税单价
			save_bodyVO.setAttributeValue("confernum", null);// 内部交易结算号
			save_bodyVO.setAttributeValue("costcenter", null);// 成本中心
			save_bodyVO.setAttributeValue("rececountryid", null);// 收货国
			save_bodyVO.setAttributeValue("buysellflag", null);// 购销类型
			save_bodyVO.setAttributeValue("triatradeflag", null);// 三角贸易
			save_bodyVO.setAttributeValue("vatcode", null);// VAT注册码
			save_bodyVO.setAttributeValue("custvatcode", null);// 客户VAT注册码
			save_bodyVO.setAttributeValue("taxcodeid", null);// 税码
			save_bodyVO.setAttributeValue("caltaxmny", null);// 计税金额
			save_bodyVO.setAttributeValue("pk_recbill", null);// 客户应收单标识
			save_bodyVO.setAttributeValue("material_src", null);// 原始物料
			save_bodyVO.setAttributeValue("matcustcode", null);// 客户物料码
			save_bodyVO.setAttributeValue("settlemoney", null);// 收款金额
			save_bodyVO.setAttributeValue("settlecurr", null);// 收款币种
			save_bodyVO.setAttributeValue("batchcode", null);// 批次号
			save_bodyVO.setAttributeValue("pk_batchcode", null);// 批次号编码
			save_bodyVO.setAttributeValue("pk_balatype", null);// 结算方式
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));
		for (int i = 0; i < aggvo.getChildrenVO().length; i++) {
			calculate(aggvo, IBillFieldGet.MONEY_DE, i);
		}
		return aggvo;
	}

	/**
	 * 金额信息计算
	 * 
	 * @throws BusinessException
	 */
	private void calculate(AggReceivableBillVO billvo, String key, int row)
			throws BusinessException {

		if (billvo.getChildrenVO() != null && billvo.getChildrenVO().length > 0) {
			ReceivableBillItemVO itemVO = (ReceivableBillItemVO) billvo
					.getChildrenVO()[row];
			String currType = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_CURRTYPE);
			String pk_org = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_ORG);
			String pk_group = (String) itemVO
					.getAttributeValue(IBillFieldGet.PK_GROUP);
			String pk_currtype = CurrencyRateUtilHelper.getInstance()
					.getLocalCurrtypeByOrgID(pk_org);

			ScaleUtils scale = new ScaleUtils(pk_group);
			IDataSetForCal data = new ArapDataDataSet(billvo, row,
					new RelationItemForCal_Debit());
			Calculator tool = new Calculator(data, scale);
			Condition cond = new Condition();

			if (pk_currtype == null) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("2006pub_0", "02006pub-0552"));
			}
			CurrtypeVO currTypeVo = CurrtypeQuery.getInstance().getCurrtypeVO(
					pk_currtype);
			String destCurrType = currTypeVo.getPk_currtype();
			cond.setCalOrigCurr(true);
			if ((IBillFieldGet.LOCAL_MONEY_CR.equals(key) || IBillFieldGet.LOCAL_MONEY_DE
					.equals(key)) && !destCurrType.equals(currType)) {// 组织当前币种为组织本币
				cond.setCalOrigCurr(false);
			}
			cond.setIsCalLocalCurr(true);
			cond.setIsChgPriceOrDiscount(false);
			cond.setIsFixNchangerate(false);
			cond.setIsFixNqtunitrate(false);
			Object buysellflag = itemVO
					.getAttributeValue(IBillFieldGet.BUYSELLFLAG);
			boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
					.equals(buysellflag)
					|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
							.equals(buysellflag);
			cond.setInternational(isInternational);

			String AP21 = SysInit.getParaString(pk_org, SysinitConst.AP21);
			cond.setIsTaxOrNet(SysinitConst.ARAP21_TAX.equals(AP21));

			cond.setGroupLocalCurrencyEnable(ArapBillCalUtil
					.isUseGroupMoney(pk_group));
			cond.setGlobalLocalCurrencyEnable(ArapBillCalUtil
					.isUseGlobalMoney());
			cond.setOrigCurToGlobalMoney(ArapBillCalUtil
					.isOrigCurToGlobalMoney());
			cond.setOrigCurToGroupMoney(ArapBillCalUtil
					.isOrigCurToGroupMoney(pk_group));
			calulateTax(itemVO);
			calulateBalance(itemVO);
			tool.calculate(cond, key);
		}

	}

	/**
	 * 处理余额信息
	 * 
	 * @param itemVO
	 */
	private void calulateBalance(ReceivableBillItemVO itemVO) {
		boolean direction = (Integer) itemVO
				.getAttributeValue(IBillFieldGet.DIRECTION) == Direction.CREDIT.VALUE
				.intValue();
		String local_money = direction ? "local_money_cr" : "local_money_de";
		String money = direction ? "money_cr" : "money_de";
		String quantity = direction ? "quantity_cr" : "quantity_de";
		String group_money = direction ? "groupcrebit" : "groupdebit";
		String global_money = direction ? "globalcrebit" : "globaldebit";
		String money_bal = "money_bal";
		String local_notax = direction ? BaseItemVO.LOCAL_NOTAX_CR
				: BaseItemVO.LOCAL_NOTAX_DE;
		String group_notax = direction ? IBillFieldGet.GROUPNOTAX_CRE
				: IBillFieldGet.GROUPNOTAX_DE;
		String global_notax = direction ? IBillFieldGet.GLOBALNOTAX_CRE
				: IBillFieldGet.GLOBALNOTAX_DE;
		String group_tax = direction ? IBillFieldGet.GROUPTAX_CRE
				: IBillFieldGet.GROUPTAX_DE;
		String global_tax = direction ? IBillFieldGet.GLOBALTAX_CRE
				: IBillFieldGet.GLOBALTAX_DE;

		itemVO.setAttributeValue(money_bal,
				itemVO.getAttributeValue(money) == null ? UFDouble.ZERO_DBL
						: itemVO.getAttributeValue(money));
		itemVO.setAttributeValue(IBillFieldGet.LOCAL_MONEY_BAL, itemVO
				.getAttributeValue(local_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(local_money));

		itemVO.setAttributeValue(IBillFieldGet.GROUPBALANCE, itemVO
				.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(group_money));

		itemVO.setAttributeValue(IBillFieldGet.GLOBALBALANCE, itemVO
				.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(global_money));
		itemVO.setAttributeValue(IBillFieldGet.QUANTITY_BAL, itemVO
				.getAttributeValue(quantity) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(quantity));
		itemVO.setAttributeValue(IBillFieldGet.OCCUPATIONMNY, itemVO
				.getAttributeValue(money_bal) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(money_bal));

		String caltaxmny_key = BillEnumCollection.TaxType.TAXOUT.VALUE
				.equals(itemVO.getAttributeValue(BaseItemVO.TAXTYPE)) ? local_notax
				: local_money;
		itemVO.setAttributeValue(IBillFieldGet.CALTAXMNY, itemVO
				.getAttributeValue(caltaxmny_key) == null ? UFDouble.ZERO_DBL
				: itemVO.getAttributeValue(caltaxmny_key));

		itemVO.setAttributeValue(
				group_tax,
				MathTool.sub(
						itemVO.getAttributeValue(group_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_money),
						(UFDouble) itemVO.getAttributeValue(group_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(group_notax)));

		itemVO.setAttributeValue(
				global_tax,
				MathTool.sub(
						itemVO.getAttributeValue(global_money) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_money),
						(UFDouble) itemVO.getAttributeValue(global_notax) == null ? UFDouble.ZERO_DBL
								: (UFDouble) itemVO
										.getAttributeValue(global_notax)));
	}

	/***
	 * 计算无税金额信息
	 * 
	 * @param itemVO
	 */
	private void calulateTax(ReceivableBillItemVO itemVO) {
		Object buysellflag = itemVO.getBuysellflag();
		boolean isInternational = BillEnumCollection.BuySellType.OUT_BUY.VALUE
				.equals(buysellflag)
				|| BillEnumCollection.BuySellType.OUT_SELL.VALUE
						.equals((buysellflag));
		UFDouble grouptaxmny = itemVO.getGroupcrebit();
		UFDouble groupnotaxmny = itemVO.getGroupnotax_cre();
		itemVO.setGrouptax_cre(MathTool.sub(grouptaxmny, groupnotaxmny));

		UFDouble globaltaxmny = itemVO.getGlobalcrebit();
		UFDouble globalnotaxmny = itemVO.getGlobalnotax_cre();
		itemVO.setGlobaltax_cre(MathTool.sub(globaltaxmny, globalnotaxmny));
		if (isInternational) {
			itemVO.setGlobalnotax_cre(itemVO.getGlobalcrebit());
			itemVO.setGroupnotax_cre(itemVO.getGroupcrebit());
			itemVO.setLocal_notax_cr(itemVO.getLocal_money_cr());
			itemVO.setNotax_cr(itemVO.getMoney_cr());
			itemVO.setCaltaxmny(itemVO.getLocal_notax_cr());
		}
	}

}
