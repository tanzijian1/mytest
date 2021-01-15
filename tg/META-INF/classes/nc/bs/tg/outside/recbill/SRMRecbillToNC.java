package nc.bs.tg.outside.recbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;

/**
 * SRM 保证金应收单相关接口
 * 
 * @author 谈子健-2020-07-22
 */
public class SRMRecbillToNC extends RecbillUtils {
	protected AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		/**
		 * SRM传过来的参数-2020-08-24-谈子健-start
		 */
		save_headVO.setPk_org(getPk_orgByCode(headVO.getPk_org()));// 财务组织
		save_headVO.setDef11(headVO.getOperator());// 经办人
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// 外系统主键
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// 外系统单号
		save_headVO.setAttributeValue("def3", headVO.getImgcode());// 影像编码
		save_headVO.setAttributeValue("def4", headVO.getImgstate());// 影像状态
		save_headVO.setAttributeValue("memo", headVO.getMemo());// 备注
		save_headVO.setAttributeValue("def13", headVO.getConfiscatedmoney());// 是否罚没
		save_headVO.setAttributeValue("bpmid", headVO.getBpmid());// bpmid
		save_headVO.setAttributeValue("def24", headVO.getMailbox());// 经办人邮箱

		/**
		 * SRM传过来的参数-2020-08-24-谈子健-end
		 */

		save_headVO.setAttributeValue("pk_fiorg", null);// 废弃财务组织
		save_headVO.setAttributeValue("pk_pcorg", null);// 利润中心
		save_headVO.setAttributeValue("sett_org", null);// 结算财务组织
		save_headVO.setAttributeValue("pk_org_v", null);// 应收财务组织版本
		save_headVO.setAttributeValue("pk_fiorg_v", null);// 废弃财务组织版本
		save_headVO.setAttributeValue("pk_pcorg_v", null);// 利润中心版本
		save_headVO.setAttributeValue("sett_org_v", null);// 结算财务组织版本
		save_headVO.setAttributeValue("isreded", null);// 是否红冲过
		save_headVO.setAttributeValue("outbusitype", null);// 外系统业务类型
		save_headVO.setAttributeValue("officialprintuser", null);// 正式打印人
		save_headVO.setAttributeValue("officialprintdate", null);// 正式打印日期
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
		save_headVO.setAttributeValue("modifiedtime", null);// 最后修改时间
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(DefaultOperator));// 创建人
		save_headVO.setAttributeValue("pk_billtype", "F0");// 单据类型编码
		save_headVO.setAttributeValue("custdelegate", null);// 代垫单位
		save_headVO.setAttributeValue("pk_corp", null);// 单位编码
		save_headVO.setAttributeValue("modifier", null);// 最后修改人
		save_headVO.setAttributeValue("pk_tradetype", "F0-Cxx-LL01");// 应收类型code
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
				getUserPkByCode(DefaultOperator));// 制单人
		save_headVO.setAttributeValue("approver", null);// 审核人
		save_headVO.setAttributeValue("approvedate", null);// 审核时间
		save_headVO.setAttributeValue("lastadjustuser", null);// 最终调整人
		save_headVO.setAttributeValue("signuser", null);// 签字人
		save_headVO.setAttributeValue("signyear", null);// 签字年度
		save_headVO.setAttributeValue("signperiod", null);// 签字期间
		save_headVO.setAttributeValue("signdate", null);// 签字日期
		save_headVO.setAttributeValue("pk_busitype", "0001ZZ10000000258BF2");// 业务流程
		save_headVO.setAttributeValue("money", headVO.getMoney());// 原币金额
		save_headVO.setAttributeValue("local_money", null);// 组织本币金额
		save_headVO.setAttributeValue("billyear", null);// 单据会计年度
		save_headVO.setAttributeValue("billperiod", null);// 单据会计期间
		save_headVO.setAttributeValue("billyear", null);// 单据会计年度
		save_headVO.setAttributeValue("scomment", null);// 摘要
		save_headVO.setAttributeValue("effectstatus", 0);// 生效状态
		save_headVO.setAttributeValue("effectuser", null);// 生效人
		save_headVO.setAttributeValue("effectdate", null);// 生效日期
		save_headVO.setAttributeValue("lastapproveid", null);// 最终审批人
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
			/**
			 * SRM传过来的参数-2020-08-24-谈子健-start
			 */
			save_bodyVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(save_headVO.getPk_balatype()));// 结算方式
			String supplier = getCustomerBySupplierCode(receivableBodyVO
					.getSupplier());
			save_bodyVO.setAttributeValue("customer", supplier);// 供应商
			String recaccount = receivableBodyVO.getRecaccount();
			save_bodyVO.setAttributeValue("recaccount",
					getBankaccnumInfo(save_headVO.getPk_org(), recaccount).get("pk_bankaccsub"));// 收款银行账户
			String payaccount = receivableBodyVO.getPayaccount();
			save_bodyVO.setAttributeValue("payaccount",
					getAccountIDByCode(payaccount, supplier, "pk_cust"));// 付款银行账户

			save_bodyVO.setAttributeValue("local_money_de",
					receivableBodyVO.getLocal_money_de());// 组织本币金额
			save_bodyVO.setAttributeValue("def10",
					receivableBodyVO.getPaymenttype());// 款项类型
			save_bodyVO.setAttributeValue("def11",
					receivableBodyVO.getDeposittype());// 招标保证金类型

			/**
			 * SRM传过来的参数-2020-08-24-谈子健-end
			 */
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
					.setAttributeValue("busidate", save_bodyVO.getBilldate());// 起算日期
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
			save_bodyVO.setAttributeValue("pk_deptid",
					save_headVO.getPk_deptid());// 部门
			save_bodyVO.setAttributeValue("pk_deptid_v", null);// 部 门
			save_bodyVO.setAttributeValue("pk_psndoc",
					save_headVO.getPk_psndoc());// 业务员
			save_bodyVO.setAttributeValue("money_de", new UFDouble(
					receivableBodyVO.getLocal_money_de()));// 借方原币金额
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
			save_bodyVO.setAttributeValue("price", save_bodyVO.getPrice());// 单价
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
			save_bodyVO.setAttributeValue("def13",
					receivableBodyVO.getContractname());// 合同名称
			save_bodyVO.setAttributeValue("def12", receivableBodyVO.getRowid());// 物业收费系统单据行ID
			save_bodyVO.setAttributeValue("def9", null);// 自定义项9
			save_bodyVO.setAttributeValue("def8", null);// 自定义项8
			save_bodyVO.setAttributeValue("pk_recbill", null);// 客户应收单标识
			save_bodyVO.setAttributeValue("def7", null);// 自定义项7
			save_bodyVO.setAttributeValue("def6", null);// 自定义项6
			save_bodyVO.setAttributeValue("def5", null);// 自定义项5
			save_bodyVO.setAttributeValue("def4", null);// 自定义项4
			save_bodyVO.setAttributeValue("def3", null);// 自定义项3
			save_bodyVO.setAttributeValue("def2", null);// 自定义项2
			save_bodyVO.setAttributeValue("def1", null);// 供应合同编号
			save_bodyVO.setAttributeValue("grouprate", null);// 集团本币汇率
			save_bodyVO.setAttributeValue("globalrate", null);// 全局本币汇率
			save_bodyVO.setAttributeValue("groupdebit", null);// 集团本币金额(借方)
			save_bodyVO.setAttributeValue("globaldebit", null);// 全局本币金额(借方)
			save_bodyVO.setAttributeValue("groupbalance", null);// 集团本币余额
			save_bodyVO.setAttributeValue("globalbalance", null);// 全局本币余额
			save_bodyVO.setAttributeValue("groupnotax_de", null);// 集团本币无税金额(借方)
			save_bodyVO.setAttributeValue("globalnotax_de", null);// 全局本币无税金额(借方)
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

			save_bodyVO.setOccupationmny(save_bodyVO.getMoney_de());// 预占用原币余额

			String budgetsub = receivableBodyVO.getBudgetsub();// 预算科目
			HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo(budgetsub);
			if (budgetsubInfo == null) {
				throw new BusinessException("预算科目[" + budgetsub
						+ "]未能在NC档案关联到相关信息!");
			}
			save_bodyVO.setAttributeValue(ReceivableBillItemVO.DEF1,
					budgetsubInfo.get("pk_obj"));//

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));

		return aggvo;
	}
}
