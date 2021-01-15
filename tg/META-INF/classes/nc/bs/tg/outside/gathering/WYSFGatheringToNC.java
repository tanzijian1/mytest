package nc.bs.tg.outside.gathering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.GatheringBodyVO;
import nc.vo.tg.outside.GatheringHeadVO;

public class WYSFGatheringToNC extends GatheringUtils {
	protected AggGatheringBillVO onTranBill(GatheringHeadVO headVO,
			List<GatheringBodyVO> bodyVOs) throws BusinessException {
		AggGatheringBillVO aggvo = new AggGatheringBillVO();
		GatheringBillVO save_headVO = new GatheringBillVO();
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		/**
		 * 物业收费系统报文传来的字段-2020-08-01-谈子健-start
		 */
		save_headVO.setDef1(headVO.getSrcid());// 外系统主键
		save_headVO.setDef2(headVO.getSrcbillno());// 外系统单号
		save_headVO.setPk_org(orgvo.getPk_org());// 财务组织
		save_headVO.setAttributeValue("billdate",
				new UFDate(headVO.getBilldate()));// 单据日期
		save_headVO.setAttributeValue("money", headVO.getMoney());// 原币金额
		save_headVO.setAttributeValue("local_money_bal", headVO.getMoney());// 组织本币余额
		save_headVO.setAttributeValue("def3", headVO.getImgcode());// 影像编码
		save_headVO.setAttributeValue("def4", headVO.getImgstate());// 影像状态
		save_headVO.setAttributeValue("def37",
				getdefdocBycode(headVO.getBusinesstype(), "SDLL003"));// 业务类型
		save_headVO.setAttributeValue("mail", headVO.getMailbox());// 经办人邮箱号
		String pk_tradetype = getPk_tradetype(headVO.getPk_tradetype());
		if (pk_tradetype == null || pk_tradetype.equals("")) {
			throw new BusinessException("交易类型编码与nc不符，联系收费系统管理员处理。");
		}
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// 收款类型code
		/**
		 * 物业收费系统报文传来的字段-2020-08-01-谈子健-end
		 */

		save_headVO.setAttributeValue("pk_fiorg", null);// 废弃财务组织
		save_headVO.setAttributeValue("pk_pcorg", null);// 利润中心
		save_headVO.setAttributeValue("sett_org", null);// 结算财务组织
		save_headVO.setAttributeValue("pk_org_v", orgvo.getPk_vid());// 应收财务组织版本
		save_headVO.setAttributeValue("pk_fiorg_v", null);// 废弃财务组织版本
		save_headVO.setAttributeValue("pk_pcorg_v", null);// 利润中心版本
		save_headVO.setAttributeValue("sett_org_v", null);// 结算财务组织版本
		save_headVO.setAttributeValue("isreded", headVO.getIsreded());// 是否红冲过
		save_headVO.setAttributeValue("outbusitype", null);// 外系统业务类型
		save_headVO.setAttributeValue("officialprintuser", null);// 正式打印人
		save_headVO.setAttributeValue("officialprintdate", null);// 正式打印日期
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
		save_headVO.setAttributeValue("modifiedtime", null);// 最后修改时间
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// 创建时间
		save_headVO.setAttributeValue("creator",
				getUserPkByCode(DefaultOperator));// 创建人
		save_headVO.setAttributeValue("pk_billtype", "F2");// 单据类型编码
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// 组织本币金额
		save_headVO.setAttributeValue("custdelegate", null);// 代垫单位
		save_headVO.setAttributeValue("pk_corp", null);// 单位编码
		save_headVO.setAttributeValue("modifier", null);// 最后修改人
		save_headVO.setAttributeValue("billclass", "sk");// 单据大类
		save_headVO.setAttributeValue("accessorynum", null);// 附件张数
		save_headVO.setAttributeValue("subjcode", null);// 科目编码
		save_headVO.setAttributeValue("isflowbill", UFBoolean.FALSE);// 是否流程单据
		save_headVO.setAttributeValue("confirmuser", null);// 单据确认人
		save_headVO.setAttributeValue("isinit", UFBoolean.FALSE);// 期初标志
		save_headVO.setAttributeValue("billno", null);// 单据号

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
		save_headVO.setAttributeValue("pk_balatype",
				getBalatypePkByCode(headVO.getPk_balatype()));// 结算方式
		// 结算号

		save_headVO.setStatus(VOStatus.NEW);
		List<GatheringBillItemVO> bodylist = new ArrayList<>();
		for (GatheringBodyVO gatheringBodyVO : bodyVOs) {
			GatheringBillItemVO save_bodyVO = new GatheringBillItemVO();
			/**
			 * 物业收费系统报文传来的字段-2020-08-01-谈子健-start
			 */
			save_bodyVO.setAttributeValue("scomment",
					gatheringBodyVO.getScomment());// 摘要
			/**
			 * 客户基本信息保存-2020-11-03-谈子健-start
			 */
			String[] onBasicBill = onBasicBill(orgvo, gatheringBodyVO);
			save_bodyVO.setAttributeValue("customer", onBasicBill[0]);// 客户
			save_bodyVO.setAttributeValue("recaccount", onBasicBill[1]);// 收款银行账号
			save_bodyVO.setAttributeValue("payaccount", onBasicBill[2]);// 付款银行账号
			// 付款银行账户

			/**
			 * 客户基本信息保存-2020-11-03-谈子健-end
			 */

			// save_bodyVO.setAttributeValue("taxrate",
			// gatheringBodyVO.getTaxrate());// 税率
			// save_bodyVO.setAttributeValue("local_tax_cr",gatheringBodyVO.getLocal_tax_cr());//
			// 税额

			save_bodyVO.setAttributeValue("def7",
					new UFDouble(gatheringBodyVO.getTaxrate()));// 税率
			save_bodyVO.setAttributeValue("def8",
					gatheringBodyVO.getLocal_tax_cr());// 税额

			save_bodyVO.setAttributeValue("taxprice",
					gatheringBodyVO.getTaxprice());// 含税单价
			save_bodyVO
					.setAttributeValue("def9", gatheringBodyVO.getNotax_cr());// 应收价款
			save_bodyVO.setAttributeValue("local_money_cr",
					gatheringBodyVO.getMoney_cr());// 本币应收金额（元）
			save_bodyVO.setAttributeValue("money_cr",
					gatheringBodyVO.getMoney_cr());// 原币应收金额（元）
			save_bodyVO.setAttributeValue("money_bal",
					gatheringBodyVO.getMoney_bal());// 应收余额
			String pk_balatype = gatheringBodyVO.getPk_balatype();
			if (pk_balatype != null && !"".equals(pk_balatype)) {
				save_bodyVO.setAttributeValue("pk_balatype",
						getBalatypePkByCode(pk_balatype));// 结算方式
			} else {
				throw new BusinessException("表体pk_balatype结算方式不能为空!");
			}

			save_bodyVO.setAttributeValue("contractno",
					gatheringBodyVO.getContractno());// 合同编号
			if (bodyVOs.size() == 1) {
				// 表头合同编码
				save_headVO.setAttributeValue("contractno",
						gatheringBodyVO.getContractno());// 合同编号
			}

			save_bodyVO.setAttributeValue("invoiceno",
					gatheringBodyVO.getInvoiceno());// 发票编号
			save_bodyVO.setAttributeValue("def18", gatheringBodyVO.getRowid());// 物业收费系统单据行ID
			save_bodyVO.setAttributeValue("def19",
					gatheringBodyVO.getContractname());// 合同名称

			String productline = gatheringBodyVO.getProductline();
			if (productline != null && !"".equals(productline)) {
				String productlineId = saveProductlineArchives(productline);// 保存产品线档案
				save_bodyVO.setAttributeValue("productline", productlineId);// 产品线
			}

			save_bodyVO.setAttributeValue("memo", gatheringBodyVO.getMemo());// 备注

			// itemcode 根据收费项目编码查询收费项目名称和收费项目类型
			String itemcode = gatheringBodyVO.getItemcode();
			if (itemcode != null && !"".equals(itemcode != null)) {
				List<Map<String, String>> budgetsubNames = getBudgetsubNameByCode(itemcode);
				for (Map<String, String> names : budgetsubNames) {
					save_bodyVO.setAttributeValue("def50",
							names.get("pk_defdoc"));// itemname 收费项目名称
					save_bodyVO.setAttributeValue("def49", names.get("pid"));// itemtype
																				// 收费项目类型
				}
			}
			// projectphase 项目阶段 自定义档案SDLL006
			save_bodyVO.setAttributeValue(
					"def16",
					getdefdocBycode(gatheringBodyVO.getProjectphase(),
							"SDLL006"));// 项目阶段

			save_bodyVO.setAttributeValue("project",
					getProject(gatheringBodyVO.getProject()));// 项目

			save_bodyVO.setAttributeValue("def43",
					getdefdocBycode(gatheringBodyVO.getArrears(), "SDLL002"));// 是否往年欠费

			save_bodyVO.setAttributeValue(
					"def44",
					getdefdocBycode(gatheringBodyVO.getBusinessbreakdown(),
							"SDLL004"));// 业务细类

			save_bodyVO.setAttributeValue(
					"def56",
					getdefdocBycode(gatheringBodyVO.getProjectproperties(),
							"SDLL007"));// 项目属性

			save_bodyVO.setAttributeValue("def51",
					getCustclassByCode(gatheringBodyVO.getPk_custclass()));// 客户分类

			String propertycode = gatheringBodyVO.getPropertycode(); // 房产编码
			String propertyname = gatheringBodyVO.getPropertyname();// 房产名称

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setAttributeValue("def52", propertycode); // 房产编码
					save_bodyVO.setAttributeValue("def53", pk_defdoc);// 房产名称
				}

			}
			String paymenttype = getdefdocBycode(
					gatheringBodyVO.getPaymenttype(), "SDLL010");
			save_bodyVO.setAttributeValue("def54", paymenttype);// 款项类型
			String subordinateyear = gatheringBodyVO.getSubordinateyear();
			// 如果和表体单据日期一样的取单据日期
			if (subordinateyear != null && !"".equals(subordinateyear)
					&& subordinateyear.contains("-")) {
				// 取月份的第一天
				int year = Integer.parseInt(subordinateyear.substring(0,
						subordinateyear.indexOf("-")));// 截取年份
				int month = Integer.parseInt(subordinateyear.substring(
						subordinateyear.indexOf("-") + 1,
						subordinateyear.length()));// 截取月份
				subordinateyear = getFirstDayOfMonth1(year, month);
				save_bodyVO.setAttributeValue("def55", new UFDate(
						subordinateyear));// 应收单所属年月
			} else {
				throw new BusinessException(
						"请检查单据应收单所属年月subordinateyear字段是否为空或格式是否正确!");
			}

			String pk_recpaytype = "";
			pk_recpaytype = getRecpaytype(getBalatypePkByCode(pk_balatype));
			save_bodyVO.setAttributeValue("pk_recpaytype", pk_recpaytype);// 收款业务类型

			if ("10".equals(pk_balatype)) {
				save_bodyVO.setAttributeValue("cashaccount",
						getCashaccountByCode(orgvo.getCode()));// 现金账户
			}
			String actualnateyear = gatheringBodyVO.getActualnateyear();// 实收所属年月-入账
			// 如果和表体单据日期一样的取单据日期
			if (actualnateyear != null && !"".equals(actualnateyear)
					&& actualnateyear.contains("-")) {
				if (headVO.getBilldate() != null
						&& headVO.getBilldate().startsWith(actualnateyear)) {
					save_bodyVO.setAttributeValue("def58",
							new UFDate(headVO.getBilldate()));// 实收所属年月
					save_bodyVO.setAttributeValue("busidate",
							new UFDate(headVO.getBilldate()));// 起算日期
				} else {
					// 如果和表体单据日期不一样的取月份的第一天
					int year = Integer.parseInt(actualnateyear.substring(0,
							actualnateyear.indexOf("-")));// 截取年份
					int month = Integer.parseInt(actualnateyear.substring(
							actualnateyear.indexOf("-") + 1,
							actualnateyear.length()));// 截取月份
					actualnateyear = getFirstDayOfMonth1(year, month);
					save_bodyVO.setAttributeValue("def58", new UFDate(
							actualnateyear));// 实收所属年月
					save_bodyVO.setAttributeValue("busidate", new UFDate(
							actualnateyear));// 起算日期
				}
			} else {
				throw new BusinessException(
						"请检查单据实收所属年月actualnateyear字段是否为空或格式是否正确!");
			}
			String def58 = (String) save_bodyVO.getAttributeValue("def58");
			if (def58 != null) {
				save_headVO.setBusidate(new UFDate(def58));
			}
			String subordiperiod = gatheringBodyVO.getSubordiperiod();// 应收归属期
			if (null != subordiperiod && !"".equals(subordiperiod)
					&& subordiperiod.contains("-")) {
				// 如果和表体单据日期不一样的取月份的第一天
				int year = Integer.parseInt(subordiperiod.substring(0,
						subordiperiod.indexOf("-")));// 截取年份
				int month = Integer
						.parseInt(subordiperiod.substring(
								subordiperiod.indexOf("-") + 1,
								subordiperiod.length()));// 截取月份
				subordiperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO.setAttributeValue("def57",
						new UFDate(subordiperiod));// 应收归属期
			} else {
				throw new BusinessException(
						"请检查单据应收归属期subordiperiod字段是否为空或格式是否正确!");
			}
			// 实收归属期
			String actualperiod = gatheringBodyVO.getActualperiod();// 应收归属期
			if (null != actualperiod && !"".equals(actualperiod)
					&& actualperiod.contains("-")) {
				// 如果和表体单据日期不一样的取月份的第一天
				int year = Integer.parseInt(actualperiod.substring(0,
						actualperiod.indexOf("-")));// 截取年份
				int month = Integer.parseInt(actualperiod.substring(
						actualperiod.indexOf("-") + 1, actualperiod.length()));// 截取月份
				actualperiod = getFirstDayOfMonth1(year, month);
				save_bodyVO
						.setAttributeValue("def59", new UFDate(actualperiod));// 实收归属期

			} else {
				throw new BusinessException(
						"请检查单据应收归属期actualperiod字段是否为空或格式是否正确!");
			}
			/**
			 * 物业收费系统报文传来的字段-2020-08-01-谈子健-end
			 */
			save_bodyVO.setAttributeValue("isdiscount", UFBoolean.FALSE);
			save_bodyVO.setAttributeValue("rate", UFDouble.ONE_DBL);
			save_bodyVO.setAttributeValue("direction",
					BillEnumCollection.Direction.CREDIT.VALUE);
			save_bodyVO.setAttributeValue("local_price", 0);
			save_bodyVO.setAttributeValue("local_taxprice", 0);
			save_bodyVO.setAttributeValue("occupationmny", 1);
			save_bodyVO.setAttributeValue("pk_billtype",
					save_bodyVO.getAttributeValue("pk_billtype"));
			save_bodyVO.setAttributeValue("pk_group", "000112100000000005FD");
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// 应收财务组织

			save_bodyVO.setAttributeValue("objtype", 0);// 往来对象
			save_bodyVO
					.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// 币种

			save_bodyVO.setOccupationmny(save_bodyVO.getMoney_cr());// 预占用原币余额

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new GatheringBillItemVO[0]));
		return aggvo;
	}

	/**
	 * 验证交易类型是否存在
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getPk_tradetype(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_billtypecode from bd_billtype where pk_billtypecode='"
						+ code + "' and nvl(dr,0)=0", new ColumnProcessor());
		return (String) obj;
	}
}
