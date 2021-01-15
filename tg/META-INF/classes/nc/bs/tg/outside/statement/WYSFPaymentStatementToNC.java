package nc.bs.tg.outside.statement;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.cmp.bill.BillAggVO;
import nc.vo.cmp.bill.BillDetailVO;
import nc.vo.cmp.bill.BillVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.LLPaymentStatementJsonBVO;
import nc.vo.tg.outside.LLPaymentStatementJsonVO;

public class WYSFPaymentStatementToNC extends PaymentStatementUtils {

	protected BillAggVO onTranBill(LLPaymentStatementJsonVO headVO,
			List<LLPaymentStatementJsonBVO> bodyVOs) throws BusinessException {
		BillAggVO aggVO = new BillAggVO();
		BillVO save_headVO = new BillVO();
		/**
		 * 物业收费系统传来的字段-2020-09-22-谈子健-start
		 */
		OrgVO orgvo = getOrgVO(headVO.getPk_org());
		if (orgvo == null || "".equals(orgvo)) {
			throw new BusinessException("财务组织Ouflag:" + headVO.getPk_org()
					+ "在NC找不到关联的数据!");
		}
		save_headVO.setPk_org(orgvo.getPk_org());// 财务组织
		save_headVO.setAttributeValue("pk_org_v", orgvo.getPk_vid());// 财务组织版本
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// 外系统主键
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// 外系统单号
		save_headVO.setAttributeValue("billdate",
				new UFDate(headVO.getBilldate()));// 单据日期

		save_headVO.setAttributeValue("local_money", headVO.getLocal_money());// 付款组织本币金额
		save_headVO.setAttributeValue("def21", headVO.getImgcode());// 影像编码
		save_headVO.setAttributeValue("mail", headVO.getMailbox());// 经办人邮箱号
		String pk_tradetype = getPk_tradetype(headVO.getPk_tradetypeid());
		if (pk_tradetype==null||pk_tradetype.equals("")) {
			throw new BusinessException("交易类型编码与nc不符，联系收费系统管理员处理。");
		}
		save_headVO.setAttributeValue("pk_tradetypeid",
				getPk_billtypeid(headVO.getPk_tradetypeid()));// 交易类型id
		save_headVO.setAttributeValue("trade_type", headVO.getPk_tradetypeid());// 交易类型
		save_headVO.setAttributeValue("def32",
				getdefdocBycode(headVO.getBusinesstype(), "SDLL003"));// 业务类型
		save_headVO.setAttributeValue("contractno", headVO.getContractno());// 合同号
		save_headVO.setAttributeValue("memo", headVO.getMemo());// 备注
		save_headVO.setAttributeValue("pk_busiflow",
				getBusitypeByCode("cmp501", "000112100000000005FD"));

		/**
		 * 物业收费系统传来的字段-2020-09-22-谈子健-end
		 */
		save_headVO.setAttributeValue("source_flag", 2);
		save_headVO.setAttributeValue("bill_date", new UFDateTime());
		save_headVO.setAttributeValue("billmaker_date", new UFDateTime());
		save_headVO.setAttributeValue("creationtime", new UFDateTime());
		save_headVO.setAttributeValue("bill_status", -1);// 单据状态
		save_headVO.setAttributeValue("effect_flag", 0);// 生效状态
		// save_headVO.setAttributeValue("paystatus", 1);// 支付状态
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
		save_headVO.setAttributeValue("billclass", "fj");
		save_headVO.setAttributeValue("billmaker",
				getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setAttributeValue("creator",
				getUserIDByCode(WYSFDefaultOperator));
		save_headVO.setBill_type("F5");
		save_headVO.setPk_billtypeid(getPk_billtypeid("F5"));
		save_headVO.setStatus(VOStatus.NEW);

		List<BillDetailVO> bodylist = new ArrayList<>();
		for (LLPaymentStatementJsonBVO bodyVO : bodyVOs) {
			BillDetailVO save_bodyVO = new BillDetailVO();
			/**
			 * 物业收费系统传来的字段表体信息-2020-09-22-谈子健-start nc.vo.cmp.bill.BillDetailVO
			 */
			String isfreecust = supplierIsfreecust(bodyVO.getPk_supplier());
			if (null != isfreecust && !"".equals(isfreecust)) {
				// 0=客户，1=供应商，2=部门，3=人员，4=散户
				if ("Y".equals(isfreecust)) {
					save_bodyVO.setAttributeValue("objecttype", 4);// 交易对象类型
					save_headVO.setAttributeValue("objecttype", 4);// 交易对象类型
				} else {
					save_bodyVO.setAttributeValue("objecttype", 1);// 交易对象类型
					save_headVO.setAttributeValue("objecttype", 1);// 交易对象类型
				}

			}
			save_bodyVO.setAttributeValue("memo", bodyVO.getMemo());// 备注
			save_bodyVO
					.setAttributeValue(
							"pk_supplier",
							getSupplierIDByCode(bodyVO.getPk_supplier(),
									save_headVO.getPk_org(),
									save_headVO.getPk_group()));// 供应商
			save_bodyVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(bodyVO.getPk_balatype()));// 结算方式

			save_bodyVO.setAttributeValue(
					"pk_oppaccount",
					getAccountIDByCode(bodyVO.getPk_oppaccount(),
							save_headVO.getPk_org(), "pk_org"));// 付款银行账户
			save_bodyVO.setAttributeValue("def10",
					getBankdocIDByCode(bodyVO.getBd_bankdoc()));// 付款单位开户行
			save_bodyVO.setAttributeValue("pay_local", bodyVO.getPay_local());// 付款组织本币金额
			save_bodyVO.setAttributeValue("def31", bodyVO.getDeposit());// 退押金人
			save_bodyVO.setAttributeValue("accounttype",
					bodyVO.getAccounttype());// 收款账户性

			int objecttype = (int) save_bodyVO.getAttributeValue("objecttype");
			// 如果是供应商
			if (objecttype == 1) {
				String account = getAccountIDByCode(bodyVO.getPk_account(),
						save_headVO.getPk_org(), "pk_org");
				if (null != account && !"".equals(account)) {
					save_bodyVO.setAttributeValue("pk_account", account);// 收款银行账号
				} else {
					throw new BusinessException("收款银行账号在NC不能关联:"
							+ bodyVO.getPk_account() + "请到SRM系统进行维护!");
				}
			} else {
				// 如果是散户直接保存
				save_bodyVO.setAttributeValue("pk_account",
						bodyVO.getPk_account());// 收款银行账号
			}
			save_bodyVO
					.setAttributeValue("accountcode", bodyVO.getPk_account());// 收款账户编码
			save_bodyVO.setAttributeValue("accountname",
					bodyVO.getAccountname());// 收款账户户名
			save_bodyVO.setAttributeValue("accountopenbank",
					bodyVO.getAccountopenbank());// 收款银行名称
			save_bodyVO.setAttributeValue("def32", bodyVO.getRowid());// 物业收费系统单据行ID
			save_bodyVO.setAttributeValue("def33",
					getProject(bodyVO.getProject()));// 项目
			save_bodyVO.setAttributeValue("def34",
					getdefdocBycode(bodyVO.getArrears(), "SDLL002"));// 【是否往年欠费】
			save_bodyVO.setAttributeValue("def35",
					getdefdocBycode(bodyVO.getBusinessbreakdown(), "SDLL004"));// 业务细类
			save_bodyVO.setAttributeValue("def36",
					getdefdocBycode(bodyVO.getItemtype(), "SDLL008"));// 收费项目类型
			save_bodyVO.setAttributeValue("def37",
					getItemnameByPk(bodyVO.getItemname(), "SDLL008"));// 收费项目名称
			save_bodyVO.setAttributeValue("def38",
					getdefdocBycode(bodyVO.getProjectphase(), "SDLL006"));// 项目阶段
			save_bodyVO.setAttributeValue("def39",
					getdefdocBycode(bodyVO.getProperties(), "SDLL007"));// 项目属性
			save_bodyVO.setAttributeValue("def40",
					getdefdocBycode(bodyVO.getPaymenttype(), "SDLL010"));// 款项类型
			save_bodyVO.setAttributeValue("def41", bodyVO.getSubordinateyear());// 应收单所属年月
			save_bodyVO.setAttributeValue("def42",
					getSupplierclassByCode(bodyVO.getSupplierclassification()));// 供应商分类
			String propertycode = bodyVO.getPropertycode(); // 房产编码
			String propertyname = bodyVO.getPropertyname();// 房产名称

			if (propertycode != null && !"".equals(propertycode)
					&& propertyname != null && !"".equals(propertyname)) {
				String pk_defdoc = saveHousePropertyArchives(propertycode,
						propertyname, orgvo.getPk_org());
				if (pk_defdoc != null && !"".equals(pk_defdoc)) {
					save_bodyVO.setAttributeValue("def43", propertycode); // 房产编码
					save_bodyVO.setAttributeValue("def44", pk_defdoc);// 房产名称
				}

			}
			/**
			 * 物业收费系统传来的字段表体信息-2020-09-22-谈子健-end nc.vo.cmp.bill.BillDetailVO
			 */
			save_bodyVO.setAttributeValue("pay_primal", bodyVO.getPay_local());// 付款原币金额
			save_bodyVO.setAttributeValue("pk_group", "000112100000000005FD");// 所属集团
			save_bodyVO.setAttributeValue("bill_date", new UFDateTime());
			save_bodyVO.setAttributeValue("creationtime", new UFDateTime());
			save_bodyVO.setAttributeValue("billclass", "fj");
			save_bodyVO.setAttributeValue("direction", -1);

			save_bodyVO
					.setAttributeValue("pk_currtype", "1002Z0100000000001K1");
			save_bodyVO.setAttributeValue("pk_org", orgvo.getPk_org());
			save_bodyVO.setAttributeValue("source_flag", 2);
			save_bodyVO.setBill_type("F5");
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
			aggVO.setParentVO(save_headVO);
			aggVO.setChildrenVO(bodylist.toArray(new BillDetailVO[0]));
		}

		return aggVO;
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
				"select pk_billtypecode from bd_billtype where pk_billtypecode='" + code
						+ "' and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}
}
