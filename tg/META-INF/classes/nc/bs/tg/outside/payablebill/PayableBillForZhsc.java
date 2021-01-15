package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SDLL-467-NC应付单对接综合商城成本接口
 * 
 * 1）F1-Cxx-LL07邻里-综合商城成本款应付单F3-Cxx-LL04邻里-综合商城零售成本款付款单
 * 
 * 2）F1-Cxx-LL11邻里-有赞星选自营零售成本应付单F3-Cxx-LL05 邻里-有赞星选零售成本款付款单
 * 
 * @author 谈子健 2020-12-28
 * 
 */

public class PayableBillForZhsc extends PayableBillUtils {

	@Override
	protected String getTradetype() {
		return null;
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("pk_org"))) {
			throw new BusinessException("财务组织pk_org不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("外系统单据主键srcid不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("外系统单据号srcbillno不可为空");
		}
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("bpmid不可为空");
		}
		if (StringUtils.isBlank(head.getString("billdate"))) {
			throw new BusinessException("单据日期billdate不可为空");
		}
		if (StringUtils.isBlank(head.getString("fdtype"))) {
			throw new BusinessException("请款类fdtype不可为空");
		}
		if (StringUtils.isBlank(head.getString("scomment"))) {
			throw new BusinessException("请款事由scomment不可为空");
		}
		if (StringUtils.isBlank(head.getString("pk_psndoc"))) {
			throw new BusinessException("业务员pk_psndoc不能为空!");
		}
		if (StringUtils.isBlank(head.getString("mail"))) {
			throw new BusinessException("业务员邮箱mail不能为空!");
		}
		if (StringUtils.isBlank(head.getString("isapproved"))) {
			throw new BusinessException("EBS预算占用是否审核通过isapproved不能为空!");
		}
		if (StringUtils.isBlank(head.getString("issmalltsaxpayer"))) {
			throw new BusinessException("是否小规模纳税人issmalltsaxpayer不能为空!");
		}
		if (StringUtils.isBlank(head.getString("hyperlinks"))) {
			throw new BusinessException("综合商城附件超链接hyperlinks不能为空!");
		}

	}

	// /**
	// * 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
	// *
	// * @param bodyvo
	// *
	// * @param ocal_tax_cr
	// * ()
	// * @throws BusinessException
	// */
	// private String getDeductibleTax(JSONObject body) throws BusinessException
	// {
	// String deductibleTax = "0";
	// String invtype = body.getString("invtype");
	// String local_tax_cr = body.getString("local_tax_cr");
	// if ("01".equals(invtype) || "08".equals(invtype)
	// || "10".equals(invtype) || "11".equals(invtype)
	// || "15".equals(invtype) || "18".equals(invtype)
	// || "19".equals(invtype)) {
	// deductibleTax = local_tax_cr;
	// }
	//
	// if ("04".equals(invtype) || "05".equals(invtype)
	// || "07".equals(invtype) || "16".equals(invtype)
	// || "17".equals(invtype)) {
	// Map<String, String> info = DocInfoQryUtils.getUtils()
	// .getDefdocInfo(invtype, "pjlx");
	// String calculation = info.get("memo");
	// String amount = body.getString("local_money_cr");
	// String sqlstr = calculation.toString().replace("amount", amount);
	// String sql = "select " + sqlstr + " from dual ";
	// Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
	// deductibleTax = Tax.toString();
	// }
	// return deductibleTax;
	// }

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);
		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("pk_org"));
		if (orgInfo == null) {
			throw new BusinessException("财务组织[" + head.getString("pk_org")
					+ "]未能在NC关联到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// 应付财务组织->NC业务单元编码
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));// bpmid
		hvo.setAttributeValue("def1", head.getString("srcid"));// 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 外系统单据号
		// 请款类型/自定义项19
		Map<String, String> fdTypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(head.getString("fdtype"), "SDLL012");
		if (fdTypeInfo == null) {
			throw new BusinessException("请款类型[" + head.getString("fdtype")
					+ "]未能在NC档案关联到相关信息!");
		}
		hvo.setAttributeValue("def19", fdTypeInfo.get("pk_defdoc"));// 请款类型
		hvo.setAttributeValue("scomment", head.getString("scomment"));// *请款事由/事由
		hvo.setAttributeValue("def22", head.getString("pk_psndoc"));// 人员(中文名称)
		hvo.setAttributeValue("def24", head.getString("mail"));// 业务员邮箱/自定义项24
		hvo.setAttributeValue("def87", head.getString("isapproved"));// EBS预算占用是否审核通过
		hvo.setAttributeValue("def44", head.getString("issmalltsaxpayer"));// 是否小规模纳税人
		hvo.setAttributeValue("hyperlinks", head.getString("hyperlinks"));// 综合商城附件超链接
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象
		hvo.setAttributeValue(PayableBillVO.EFFECTSTATUS, 0);// 生效状态
	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// 税率
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// 借方原币金额 //
		// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, body
				.getString("local_notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_cr")));// // 组织本币无税金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// 税额->付款计划明细不含税金额*税率
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// 供应商
		itemVO.setAttributeValue(PayableBillItemVO.DEF26,
				body.getString("rowid"));
		itemVO.setAttributeValue(PayableBillItemVO.DEF14, DocInfoQryUtils
				.getUtils().getDefdocInfo("A3", "zdy020"));// 默认结算款编码为:A3

		String budgetsub = body.getString("budgetsub");// 预算科目
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("预算科目[" + budgetsub
					+ "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));// 预算科目主键

		String project = body.getString("project");// 项目名称
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("项目名称[" + project + "]未能在NC档案关联到相关信息!");
		}

		itemVO.setAttributeValue(PayableBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// 项目名称

		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(),
				headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + body.getString("supplier")
					+ "】未能在NC档案查询到相关信息");
		}
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// 供应商

		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getCustAccnumInfo(pk_supplier, body.getString("recaccount"));// 收款银行账户

		if (recaccountInfo == null) {
			throw new BusinessException("收款银行账户["
					+ body.getString("recaccount") + "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// 收款银行账户
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,
				recaccountInfo.get("bankname"));// 开户行名称/自定义项28

		itemVO.setAttributeValue(PayableBillItemVO.DEF3,
				body.getString("deductibletax"));// 可抵扣税额

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// 往来对象
	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("rowid"))) {
			throw new BusinessException("行主键rowid不可为空");
		}
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("预算科目budgetsub不可为空");
		}
		if (StringUtils.isBlank(body.getString("project"))) {
			throw new BusinessException("项目project不可为空");
		}
		if (StringUtils.isBlank(body.getString("supplier"))) {
			throw new BusinessException("供应商supplier不可为空");
		}
		if (StringUtils.isBlank(body.getString("recaccount"))) {
			throw new BusinessException("收款银行账号recaccount不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
			throw new BusinessException("无税金额local_notax_cr不可为空");
		}
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("税率taxrate不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("税额local_tax_cr不可为空");
		}
		if (StringUtils.isBlank(body.getString("invoice_money"))) {
			throw new BusinessException("发票金额invoice_money不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("含税金额local_money_cr不可为空");
		}

	}

}
