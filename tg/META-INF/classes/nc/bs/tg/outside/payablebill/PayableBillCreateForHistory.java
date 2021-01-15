package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * SRM系统推送应付单数据到NC财务系统接口 外部接口无对历史信息对接
 * 
 * @author ASUS
 * 
 */
@Deprecated
public class PayableBillCreateForHistory extends PayableBillUtils {

	/**
	 * 邻里-员工代历史供应商合同\非合同请款
	 */
	@Override
	protected String getTradetype() {
		return "F1-Cxx-LL01";
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("org"))) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(head.getString("billamount"))) {
			throw new BusinessException("本次票据金额不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("EBS主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("EBS单据号不可为空");
		}
		if (StringUtils.isBlank(head.getString("contcode"))) {
			throw new BusinessException("合同编码不可为空");
		}
		if (StringUtils.isBlank(head.getString("contname"))) {
			throw new BusinessException("合同名称不可为空");
		}
		if (StringUtils.isBlank(head.getString("conttype"))) {
			throw new BusinessException("合同类型不可为空");
		}
		if (StringUtils.isBlank(head.getString("plate"))) {
			throw new BusinessException("板块不可为空");
		}
		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(head.getString("proejctdata"))) {
			throw new BusinessException("项目不可为空");
		}
		if (StringUtils.isBlank(head.getString("proejctstages"))) {
			throw new BusinessException("项目分期不可为空");
		}
		if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
			throw new BusinessException("累计发票金额不可为空");
		}
		if (StringUtils.isBlank(head.getString("auditstate"))) {
			throw new BusinessException("地区财务审批状态不可为空");
		}
		// if (StringUtils.isBlank(head.getString("billdate"))) {
		// throw new BusinessException("单据日期不可为空");
		// }
		if (StringUtils.isBlank(head.getString("def55"))) {
			throw new BusinessException("EBS请款方式不能为空!");
		}

	}

	/**
	 * 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
	 * 
	 * @param bodyvo
	 * 
	 * @param ocal_tax_cr
	 *            ()
	 * @throws BusinessException
	 */
	private String getDeductibleTax(JSONObject body) throws BusinessException {
		String deductibleTax = "0";
		String invtype = body.getString("invtype");
		String local_tax_cr = body.getString("local_tax_cr");
		if ("01".equals(invtype) || "08".equals(invtype)
				|| "10".equals(invtype) || "11".equals(invtype)
				|| "15".equals(invtype) || "18".equals(invtype)
				|| "19".equals(invtype)) {
			deductibleTax = local_tax_cr;
		}

		if ("04".equals(invtype) || "05".equals(invtype)
				|| "07".equals(invtype) || "16".equals(invtype)
				|| "17".equals(invtype)) {
			Map<String, String> info = DocInfoQryUtils.getUtils()
					.getDefdocInfo(invtype, "pjlx");
			String calculation = info.get("memo");
			String amount = body.getString("local_money_cr");
			String sqlstr = calculation.toString().replace("amount", amount);
			String sql = "select " + sqlstr + " from dual ";
			Object Tax = getBaseDAO().executeQuery(sql, new ColumnProcessor());
			deductibleTax = Tax.toString();
		}
		return deductibleTax;
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

		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", head.getString("imgno"));// 自定义项3
																// 影像编码->影像编码
		hvo.setAttributeValue("def4", head.getString("imgstate"));// 自定义项4
																	// 影像状态->影像状态
		hvo.setAttributeValue("def5", head.getString("contcode"));// 自定义项5
																	// 合同编码->合同编码
		hvo.setAttributeValue("def6", head.getString("contname"));// 合同名称->合同名称
																	// def6
		hvo.setAttributeValue("def7", head.getString("conttype"));// 自定义项7
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -start
		 */
		String conttype = head.getString("conttype");
		Map<String, String> conttypeInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(conttype, "zdy045");
		if (conttypeInfo == null) {
			conttypeInfo = DocInfoQryUtils.getUtils().getDefdocInfo(conttype,
					"zdy008");
		}
		if (conttypeInfo != null) {
			hvo.setAttributeValue("def56", conttypeInfo.get("pk_defdoc"));// 自定义项8
		}
		/**
		 * 成本应付单接口，自动根据合同类型带出应付单表头“财务票据类型”（NC字段-def56）,参照档案zdy008-2020-05-08-谈子健
		 * -end
		 */
		// 合同细类->合同细类
		// 合同类型->合同类型
		hvo.setAttributeValue("def8", head.getString("contcell"));// 自定义项8
																	// 合同细类->合同细类
		// 紧急程度->紧急程度2020-02-18-tzj
		String emergency = head.getString("mergency");
		Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
				.getDefdocInfo(emergency, "zdy031");
		if (emergencyInfo != null) {
			hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));// 自定义项9
		}
		hvo.setAttributeValue("def10", "SRM");// 自定义项10 来源外部系统

		if (StringUtils.isNotBlank(head.getString("plate"))) {
			Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(head.getString("plate"), "bkxx");
			if (plateInfo == null) {
				throw new BusinessException("板块【" + head.getString("plate")
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// 自定义项15
																		// 板块
		}
		if (StringUtils.isNotBlank(head.getString("accorg"))) {
			Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
					.getOrgInfo(head.getString("accorg"));
			if (accorgInfo == null) {
				throw new BusinessException("出账公司【" + head.getString("accorg")
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// 自定义项17
			// 出账公司->NC业务单元编码
		}
		// 新增供应商2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				head.getString("supplier"), hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + head.getString("supplier")
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue("supplier", pk_supplier);// 供应商

		// hvo.setPk_busitype(null);//交易类型
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

		if (StringUtils.isNotBlank(head.getString("proejctdata"))) {
			HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
					.getSpecialProjectInfo(head.getString("proejctdata"));
			if (projectInfo == null) {
				throw new BusinessException("项目【"
						+ head.getString("proejctdata") + "】未能在NC档案关联到相关信息!");

			}
			hvo.setAttributeValue("def19", projectInfo.get("pk_project"));// 项目
			// 应付单由传入项目信息带出是否资本(def8),项目资质(def9)
			hvo.setAttributeValue("def61", projectInfo.get("def8"));// 是否资本
			hvo.setAttributeValue("def62", projectInfo.get("def9"));// 项目性质

		}

		if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
			HashMap<String, String> projectstagesInfo = DocInfoQryUtils
					.getUtils().getSpecialProjectInfo(
							head.getString("proejctstages"));
			if (projectstagesInfo == null) {
				throw new BusinessException("项目分期 【"
						+ head.getString("proejctstages") + "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def32", projectstagesInfo.get("pk_project"));// 自定义项32
																				// 项目分期
		}
		hvo.setAttributeValue("money",
				head.getString("billamount") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("billamount")));// 本次票据金额
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// 累计请款金额
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// 累计付款金额
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// 是否先付款后补票
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// 本次请款累计付款金额
		hvo.setAttributeValue("def26",
				head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("totalmny_inv")));// 累计发票金额
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// 是否有质保金扣除
		hvo.setAttributeValue("def31", head.getString("note"));// 说明
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// 地区财务审批状态
		if (StringUtils.isNotBlank(head.getString("auditstate"))) {
			Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(head.getString("auditstate"), "zdy032");
			if (auditstateInfo == null) {
				throw new BusinessException("地区财务审批状态 【"
						+ head.getString("auditstate") + "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));// 自定义项32
																			// 项目分期
		}
		hvo.setAttributeValue("def43", head.getString("def43"));// 非标准指定付款涵def43
		hvo.setAttributeValue("def44", head.getString("def44"));// 补附件def44
		hvo.setAttributeValue("def45", head.getString("def45"));// 已补全def45
		hvo.setAttributeValue("def46", head.getString("dept"));// 经办部门
		hvo.setAttributeValue("def47", head.getString("psndoc"));// 经办人
		hvo.setAttributeValue("def50", "N");// 票据权责发生制
		hvo.setAttributeValue("def55", head.getString("def55"));// EBS请款方式-def55

		if (StringUtils.isNotBlank(head.getString("signorg"))) {
			Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
					.getOrgInfo(head.getString("signorg"));
			if (signOrgInfo == null) {
				throw new BusinessException("签约公司【" + head.getString("signorg")
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// 签约公司
		}
		/**
		 * 成本应付单添加结算方式-2020-05-26-谈子健
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式

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
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// 摘要

		if (StringUtils.isNotBlank(body.getString("subjcode"))) {
			String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
					body.getString("subjcode"), headVO.getPk_org());
			if (pk_subjcode == null) {
				throw new BusinessException("会计科目["
						+ body.getString("subjcode") + "]未能在NC档案关联到相关信息!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// 科目编码
																				// 会计科目
		}
		if (StringUtils.isNotBlank(body.getString("costsubject"))) {
			Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(body.getString("costsubject"), "zdy024");
			if (costsubjectInfo == null) {
				throw new BusinessException("成本科目["
						+ body.getString("costsubject") + "]未能在NC档案关联到!");
			}
			itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));// 成本科目

		}

		if (StringUtils.isNotBlank(body.getString("invtype"))) {
			Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(body.getString("invtype"), "pjlx");
			if (invtypetInfo == null) {
				throw new BusinessException("票种[" + body.getString("invtype")
						+ "]未能在NC档案关联到!");
			}
			itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
		}
		// 设置业态 def3
		itemVO.setAttributeValue("def3", body.getString("format"));
		// 设置比例 def4
		UFDouble formatratio = body.getString("formatratio") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("formatratio"));
		itemVO.setAttributeValue("def4", formatratio.multiply(100).toString());
		// 财务票据类型
		itemVO.setAttributeValue("def9", "100112100000000069WH");
		// 成本应付单表体def15可抵扣税额，根据发票类型来重新计算-2020-04-24-谈子健
		String deductibleTax = getDeductibleTax(body);
		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * 成本应付单添加结算方式-2020-06-04-谈子健
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// 结算方式

	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		// if (StringUtils.isBlank(body.getString("subjcode"))) {
		// throw new BusinessException("会计科目不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("inoutbusiclass"))) {
		// throw new BusinessException("收支项目不可为空");
		// }
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("税率不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
			throw new BusinessException("无税金额不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("税额不可为空");
		}
		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
			throw new BusinessException("价税合计不可为空");
		}
		// if (StringUtils.isBlank(body.getString("paymenttype"))) {
		// throw new BusinessException("款项类型不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("format"))) {
		// throw new BusinessException("业态不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("比例不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("recaccount"))) {
		// throw new BusinessException("收款方开户行行号不可为空");
		// }
		if (StringUtils.isBlank(body.getString("costsubject"))) {
			throw new BusinessException("成本科目不可为空");
		}
		if (StringUtils.isBlank(body.getString("invtype"))) {
			throw new BusinessException("票据类型(票种)不可为空");
		}
		// if (StringUtils.isBlank(body.getString("scomment"))) {
		// throw new BusinessException("摘要不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("supplier"))) {
		// throw new BusinessException("供应商不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("预算科目不可为空");
		// }
	}

}
