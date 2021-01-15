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
 * SRM系统推送应付单数据到NC财务系统接口
 * 
 * @author ASUS
 * 
 */
public class PayableBillCreateForNormal extends PayableBillUtils {
	/**
	 * 邻里-SRM供应商请款应付单
	 */
	@Override
	protected String getTradetype() {
		// TODO 自动生成的方法存根
		return "F1-Cxx-LL01";
	}

	public String getDefaultoperator() {
		return "LLBPM";
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("出账公司不可为空");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("请款日期不可为空");
		// }
		if (StringUtils.isBlank(head.getString("bpmid"))) {
			throw new BusinessException("BPMID不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("请款单据号不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("合同编号不可为空");
		}
		// if (StringUtils.isBlank(head.getString("contname"))) {
		// throw new BusinessException("*合同名称不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("conttype"))) {
		// throw new BusinessException("合同类型不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("contcell"))) {
		// throw new BusinessException("合同细类不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("orderbillno"))) {
		// throw new BusinessException("采购订单号不可为空");
		// }
		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("供应商名称不可为空");
		}
		// if (StringUtils.isBlank(head.getString("bankname"))) {
		// throw new BusinessException("开户行名称不可为空");
		// }
		// if (StringUtils.isBlank(head.getString("bankcode"))) {
		// throw new BusinessException("开户行编码不可为空");
		// }
		if (StringUtils.isBlank(head.getString("recaccount"))) {
			throw new BusinessException("收款方账号不可为空");
		}
		if (StringUtils.isBlank(head.getString("scomment"))) {
			throw new BusinessException("*请款事由不可为空");
		}
		if (StringUtils.isBlank(head.getString("fdtype"))) {
			throw new BusinessException("请款类型不可为空");
		}
		if (StringUtils.isBlank(head.getString("psndoc"))) {
			throw new BusinessException("申请人不可为空");
		}
		if (StringUtils.isBlank(head.getString("psnmail"))) {
			throw new BusinessException("申请人邮箱不可为空");
		}
		if (StringUtils.isBlank(head.getString("imgno"))) {
			throw new BusinessException("影像编码不可为空");
		}
		// if (StringUtils.isBlank(head.getString("imgstate"))) {
		// throw new BusinessException("影像状态不可为空");
		// }
		if (StringUtils.isBlank(head.getString("iscycle"))) {
			throw new BusinessException("是否周期性计提不可为空");
		}
		if (StringUtils.isBlank(head.getString("isshare"))) {
			throw new BusinessException("是否分摊合同不可为空");
		}

	}

	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("出账公司[" + head.getString("org")
					+ "]未能在NC关联到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_ORG, orgInfo.get("pk_org"));// 应付财务组织->NC业务单元编码

		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象
		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", head.getString("imgno"));// 自定义项3
																// 影像编码->影像编码
		hvo.setAttributeValue("def4", head.getString("imgstate"));// 自定义项4
																	// 影像状态->影像状态

		Map<String, String> contInfo = DocInfoQryUtils.getUtils()
				.getPayContInfo(head.getString("contcode"));
		if (contInfo == null) {
			throw new BusinessException("合同[" + head.getString("contcode")
					+ "]未能在NC付款合同关联到相关信息");
		}

		hvo.setAttributeValue("def5", contInfo.get("contcode"));// 自定义项5
																// 合同编码->合同编码
		hvo.setAttributeValue("def6", contInfo.get("contname"));// 合同名称->合同名称
																// def6
		hvo.setAttributeValue("def7", contInfo.get("conttype"));// 自定义项7/合同类型
		// 合同细类->合同细类
		hvo.setAttributeValue("def8", contInfo.get("contcell"));// 自定义项8
																// 合同细类->合同细类

		hvo.setAttributeValue("def83", contInfo.get("conttypeid"));// 自定义项7/合同类型ID
		// 合同细类->合同细类
		hvo.setAttributeValue("def84", contInfo.get("contcellid"));// 自定义项8
																	// 合同细类->合同细类ID

		// String pk_supplier = contInfo.get("supplier");
		// if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
		// throw new BusinessException("付款合同【" + head.getString("contcode")
		// + "】 无收款方信息!");
		// }
		// hvo.setAttributeValue("supplier", pk_supplier);// 供应商

		hvo.setAttributeValue("def9", head.getString("orderbillno"));// 采购订单号/自定义项9
		hvo.setAttributeValue("def10", "SRM");// 自定义项10/来源外部系统
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + head.getString("supplier")
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// 供应商

		// 收款银行账户
		// Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
		// .getBankaccnumInfo(hvo.getPk_org(),
		// head.getString("recaccount"));
		Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
				.getCustAccnumInfo(pk_supplier, head.getString("recaccount"));

		if (recaccountInfo == null) {
			throw new BusinessException("收款银行账户["
					+ head.getString("recaccount") + "]未能在NC档案关联到相关信息!");
		}
		hvo.setAttributeValue(PayableBillVO.RECACCOUNT,
				recaccountInfo.get("pk_bankaccsub"));// 收款银行账户

		hvo.setAttributeValue("def28", recaccountInfo.get("bankname"));// 开户行名称/自定义项28
		hvo.setAttributeValue("def25", recaccountInfo.get("bankcode"));// 开户行编码/自定义项25

		hvo.setAttributeValue("scomment", head.getString("scomment"));// *请款事由/事由
		// 请款类型/自定义项19
		Map<String, String> fdTypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(head.getString("fdtype"), "SDLL012");
		if (fdTypeInfo == null) {
			throw new BusinessException("请款类型[" + head.getString("fdtype")
					+ "]未能在NC档案关联到相关信息!");
		}
		hvo.setAttributeValue("def19", fdTypeInfo.get("pk_defdoc"));// *请款事由/事由

		// 申请人/业务员
		// Map<String, String> psnInfo = DocInfoQryUtils.getUtils().getPsnInfo(
		// head.getString("psndoc"));
		// if (psnInfo == null) {
		// throw new BusinessException(" 申请人[" + head.getString("psndoc")
		// + "]未能在NC档案关联到相关信息!");
		// }
		// hvo.setAttributeValue("pk_psndoc", psnInfo.get("pk_psndoc"));// 申请人
		String psndoc = head.getString("psndoc");// 人员
		hvo.setAttributeValue(PayableBillVO.DEF11, psndoc);

		hvo.setAttributeValue("def24", head.getString("psnmail"));// 业务员邮箱/自定义项24
		hvo.setAttributeValue("def20", head.getString("iscycle"));// 是否周期性计提/自定义项20
		hvo.setAttributeValue("def16", head.getString("isshare"));// 是否分摊合同/自定义项16
		hvo.setAttributeValue(
				"def44",
				head.getString("issmalltsaxpayer") == null ? "N" : head
						.getString("issmalltsaxpayer"));// 是否小规模纳税人
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
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// 供应商
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				headVO.getScomment());// 摘要
		itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
				headVO.getRecaccount());

		String budgetsub = body.getString("budgetsub");// 预算科目
		HashMap<String, String> budgetsubInfo = DocInfoQryUtils.getUtils()
				.getBudgetsubInfo(budgetsub);
		if (budgetsubInfo == null) {
			throw new BusinessException("预算科目[" + budgetsub
					+ "]未能在NC档案关联到相关信息!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.DEF1,
				budgetsubInfo.get("pk_obj"));//

		String project = body.getString("budgetproject");// 项目名称
		HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
				.getProjectInfo(project);
		if (projectInfo == null) {
			throw new BusinessException("项目名称[" + project + "]未能在NC档案关联到相关信息!");
		}

		itemVO.setAttributeValue(PayableBillItemVO.PROJECT,
				projectInfo.get("pk_project"));// 项目名称

		itemVO.setAttributeValue(PayableBillItemVO.DEF4,
				body.getString("formatratio"));// 拆分比例/自定项4

		itemVO.setAttributeValue(PayableBillItemVO.DEF6,
				body.getString("budgetyear"));// 预算年度/自定项6
		itemVO.setAttributeValue(PayableBillItemVO.DEF7,
				body.getString("budgetmoney"));// 预算金额（元）/自定项7

		itemVO.setAttributeValue(PayableBillItemVO.INVOICENO,
				body.getString("invoicecode"));// 发票编号

		String invoicetype = body.getString("invoicetype");
		if (StringUtils.isNotBlank(invoicetype)) {
			Map<String, String> invTypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(invoicetype, "pjlx");
			if (invTypeInfo == null) {
				throw new BusinessException("发票类型[" + invoicetype
						+ "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF2,
					invTypeInfo.get("pk_defdoc"));// 发票类型/自定项2
		}

		itemVO.setAttributeValue(PayableBillItemVO.DEF20,
				body.getString("invoicenumber"));// 发票份数

		itemVO.setAttributeValue(PayableBillItemVO.DEF11,
				body.getString("invoicedate"));// 开票日期

		UFDouble local_money_cr = body.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr"));// 含税金额（此次请款金额）

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, UFDouble.ZERO_DBL);// 税率
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR,
				local_money_cr);// 不含税金额
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, local_money_cr);// 借方原币金额
																				// //
		// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR,
				UFDouble.ZERO_DBL);// 税额->付款计划明细不含税金额*税率

		String local_tax_cr = body.getString("local_tax_cr");// 税额
		String taxrate = body.getString("taxrate");// 税率
		String local_notax_cr = body.getString("local_notax_cr");// 不含税金额
		String local_moeny_inv = body.getString("local_moeny_inv");// 发票金额
		itemVO.setAttributeValue("def81", local_tax_cr);// 税额
		itemVO.setAttributeValue("def82", local_notax_cr);// 不含税金额
		itemVO.setAttributeValue("def83", taxrate);// 税率
		itemVO.setAttributeValue("def60", local_moeny_inv);// 发票金额

		/**
		 * 收款银行账户-2020-09-24-谈子健
		 */
		itemVO.setAttributeValue("recaccount", headVO.getRecaccount());// 收款银行账户

		String fundtype = body.getString("fundtype");
		if (StringUtils.isNotBlank(fundtype)) {
			Map<String, String> fundTypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(fundtype, "zdy020");
			if (fundtype == null) {
				throw new BusinessException("款项性质[" + fundtype
						+ "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF14,
					fundTypeInfo.get("pk_defdoc"));// 款项性质/自定项2
		}

		itemVO.setAttributeValue(PayableBillItemVO.DEF8, headVO.getDef19());// *请款事由/事由

		itemVO.setAttributeValue(PayableBillItemVO.DEF3,
				body.getString("moeny_deduction"));// 可抵扣税额

		itemVO.setAttributeValue(PayableBillItemVO.DEF26,
				body.getString("srcbodyid"));// 接收行ID
	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		if (StringUtils.isBlank(body.getString("budgetsub"))) {
			throw new BusinessException("预算科目不可为空");
		}
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("拆分比例不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetyear"))) {
		// throw new BusinessException("预算年度不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetmoney"))) {
		// throw new BusinessException("预算金额（元）不可为空");
		// }
		if (StringUtils.isBlank(body.getString("budgetproject"))) {
			throw new BusinessException("项目名称不可为空");
		}
		// if (StringUtils.isBlank(body.getString("invoicecode"))) {
		// throw new BusinessException("发票编号不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("invoicetype"))) {
		// throw new BusinessException("发票类型不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("fundtype"))) {
		// throw new BusinessException("款项性质不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("invoicenumber"))) {
		// throw new BusinessException("发票份数不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("invoicedate"))) {
		// throw new BusinessException("开票日期不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
		// throw new BusinessException("不含税金额不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("taxrate"))) {
		// throw new BusinessException("税率%不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
		// throw new BusinessException("税额不可为空");
		// }
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("含税金额不可为空");
		}
		// if (StringUtils.isBlank(body.getString("moeny_deduction"))) {
		// throw new BusinessException("可抵扣税额不可为空");
		// }

	}

}
