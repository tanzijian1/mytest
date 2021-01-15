package nc.bs.tg.outside.estipayablebill;

import java.util.Map;

import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.estipayable.EstiPayableBillItemVO;
import nc.vo.arap.estipayable.EstiPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 暂估应付单
 * 
 * @author ASUS
 * 
 */
public class EsPayablebillCreateForReturnImpl extends EstiPayableBillUtils {
	/**
	 * 邻里-SRM退货单
	 */
	@Override
	protected String getTradetype() {
		return "23E1-Cxx-LL02";
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
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("到货单主键不可为空");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("到货单编码不可为空");
		}

		if (StringUtils.isBlank(head.getString("supplier"))) {
			throw new BusinessException("供应商不可为空");
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
	}

	/**
	 * 检验行信息必录
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("预算科目不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("formatratio"))) {
		// throw new BusinessException("拆分比例不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetyear"))) {
		// throw new BusinessException("预算年度不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetmoney"))) {
		// throw new BusinessException("预算金额（元）不可为空");
		// }
		// if (StringUtils.isBlank(body.getString("budgetproject"))) {
		// throw new BusinessException("项目名称不可为空");
		// }
		if (StringUtils.isBlank(body.getString("money_cr"))) {
			throw new BusinessException("接收金额不可为空");
		}
		if (StringUtils.isBlank(body.getString("quantity_cr"))) {
			throw new BusinessException("数量不可为空");
		}
		if (StringUtils.isBlank(body.getString("srmid"))) {
			throw new BusinessException("主ID不可为空");
		}
	}

	@Override
	protected void setHeaderVO(EstiPayableBillVO hvo, JSONObject head)
			throws BusinessException {
		checkHeaderNotNull(head);

		Map<String, String> orgInfo = DocInfoQryUtils.getUtils().getOrgInfo(
				head.getString("org"));
		if (orgInfo == null) {
			throw new BusinessException("出账公司[" + head.getString("org")
					+ "]未能在NC关联到相关信息");
		}
		hvo.setAttributeValue(EstiPayableBillVO.PK_ORG, orgInfo.get("pk_org"));// 应付财务组织->NC业务单元编码

		hvo.setAttributeValue(IBillFieldGet.ESTFLAG,
				BillEnumCollection.EstiType.EST.VALUE);// 单据状态
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// 自定义项2
																	// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", head.getString("imgno"));// 自定义项3
																// 影像编码->影像编码
		hvo.setAttributeValue("def4", head.getString("imgstate"));// 自定义项4
																	// 影像状态->影像状态

		// hvo.setAttributeValue("def5", head.getString("contcode"));// 自定义项5
		// // 合同编码->合同编码
		// hvo.setAttributeValue("def6", head.getString("contname"));//
		// 合同名称->合同名称
		// // def6
		// hvo.setAttributeValue("def7", head.getString("conttype"));// 自定义项7

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
		hvo.setAttributeValue("def7", contInfo.get("conttypeid"));// 自定义项7/合同类型ID
		// 合同细类->合同细类
		hvo.setAttributeValue("def8", contInfo.get("contcellid"));// 自定义项8

		// String pk_supplier = contInfo.get("supplier");
		// if (StringUtils.isBlank(pk_supplier) || "~".equals(pk_supplier)) {
		// throw new BusinessException("付款合同【" + head.getString("contcode")
		// + "】 无收款方信息!");
		// }
		// hvo.setAttributeValue("supplier", pk_supplier);// 供应商

		hvo.setAttributeValue("def10", "SRM");// 自定义项10 来源外部系统

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

		/**
		 * 成本应付单添加结算方式-2020-05-26-谈子健
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// 结算方式

	}

	@Override
	protected void setItemVO(EstiPayableBillVO headVO,
			EstiPayableBillItemVO itemVO, JSONObject body)
			throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		itemVO.setAttributeValue(EstiPayableBillItemVO.BILLDATE,
				headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(EstiPayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// 部门
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// 部 门
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(EstiPayableBillItemVO.OBJTYPE,
				headVO.getObjtype());// 往来对象

		itemVO.setAttributeValue(EstiPayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// 方向

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(EstiPayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// 挂起标志
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// 购销类型
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// 三角贸易
		itemVO.setAttributeValue(EstiPayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// 币种
		itemVO.setAttributeValue(EstiPayableBillItemVO.RATE, headVO.getRate());// 组织本币汇率
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// 收货国

		itemVO.setAttributeValue(EstiPayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// 税率
		itemVO.setAttributeValue(EstiPayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(EstiPayableBillItemVO.MONEY_CR, body
				.getString("money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_cr")));// 借方原币金额 //
		// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 本币金额
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_NOTAX_CR,
				itemVO.getMoney_cr());// // 组织本币无税金额
		itemVO.setAttributeValue(EstiPayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// 税额->付款计划明细不含税金额*税率

		itemVO.setAttributeValue(EstiPayableBillItemVO.SUPPLIER,
				headVO.getSupplier());// 供应商
		itemVO.setAttributeValue(EstiPayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// 摘要
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// 结算方式

		itemVO.setAttributeValue(EstiPayableBillItemVO.QUANTITY_CR, body
				.getString("quantity_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("quantity_cr")));// 税额->付款计划明细不含税金额*税率

		itemVO.setAttributeValue("def21", body.getString("srmid"));// SRM行主键

		if (!StringUtils.isBlank(body.getString("materieltype"))) {
			String materieltype = DocInfoQryUtils.getUtils()
					.getMaterielTypeInfo(body.getString("materieltype"));
			if (materieltype == null) {
				throw new BusinessException("物料分类[" + materieltype
						+ "]未在NC关联到信息");
			}
			itemVO.setAttributeValue("def1", materieltype);// SRM物料分类

		}

	}
}
