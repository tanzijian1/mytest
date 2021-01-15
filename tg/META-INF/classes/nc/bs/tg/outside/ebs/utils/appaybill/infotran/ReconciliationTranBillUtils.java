package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import org.apache.commons.lang.StringUtils;

/**
 * SRM对账单
 * 
 * @author ASUS
 * 
 */
public class ReconciliationTranBillUtils extends BillTranUtils {
	static ReconciliationTranBillUtils utils;

	public static ReconciliationTranBillUtils getUtils() {
		if (utils == null) {
			utils = new ReconciliationTranBillUtils();
		}
		return utils;
	}

	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			PayBillItemVO bodyvo) throws BusinessException {
		checkItemNotNull(bodyvo);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// 所属集团
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// 单据类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// 交易类型
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// 交易类型ID
		// itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
		// headVO.getBilldate());// 单据日期
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// 起算日期

		// itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
		// headVO.getPk_deptid());// 部门
		// itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
		// headVO.getPk_deptid_v());// 部 门
		// itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
		// headVO.getPk_psndoc());// 业务员

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

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

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, bodyvo.getTaxrate());// 税率
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// 扣税类别
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR, bodyvo
				.getLocal_money_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_money_cr()));// 借方原币金额 //
															// 价税合计->付款计划明细金额
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR,
				itemVO.getMoney_cr());// 供应商开票（含税）
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, bodyvo
				.getLocal_notax_cr() == null ? UFDouble.ZERO_DBL
				: new UFDouble(bodyvo.getLocal_notax_cr()));// // 供应商开票（不含税）
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, bodyvo
				.getLocal_tax_cr() == null ? UFDouble.ZERO_DBL : new UFDouble(
				bodyvo.getLocal_tax_cr()));// 供应商开票（税额）

		String pk_supplier = headVO.getSupplier();
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// 供应商

		itemVO.setAttributeValue("def19", "100112100000000071JH");// 项目(默认0409)
		itemVO.setAttributeValue("def22", bodyvo.getInvposted());// 发票已入账标记
		itemVO.setAttributeValue("def23", bodyvo.getArrimoney());// 到货接收金额（不含税）
		/*
		 * // 票种 if (bodyvo.getChecktype() != null) { String pk_checktype =
		 * getNotetypeByCode(bodyvo.getChecktype()); if (pk_checktype == null) {
		 * throw new BusinessException("票据类型[" + bodyvo.getChecktype() +
		 * "]未能在NC档案关联到相关信息!"); }
		 * itemVO.setAttributeValue(PayableBillItemVO.CHECKTYPE,
		 * pk_checktype);// 票据类型 }
		 * 
		 * // 票种 if (bodyvo.getChecktype() != null) { String pk_checktype =
		 * getdefdocBycode(bodyvo.getChecktype(),"pjlx" ); if (pk_checktype ==
		 * null) { throw new BusinessException("票种[" + bodyvo.getInvtype() +
		 * "]未能在NC档案关联到!"); } itemVO.setAttributeValue("def8", pk_checktype);//
		 * } if (bodyvo.getMny_accadj() != null) {
		 * itemVO.setAttributeValue("def10", bodyvo.getMny_accadj());// 对账调整（含税）
		 * } if (bodyvo.getMny_notax_accadj() != null) {
		 * itemVO.setAttributeValue("def11", bodyvo.getMny_notax_accadj());//
		 * 对账调整（不含税） } if (bodyvo.getMny_notax_accadj() != null) {
		 * itemVO.setAttributeValue("def12", bodyvo.getTax_accadj());// 对账调整（税额）
		 * }
		 */
		// itemVO.setAttributeValue("def27", bodyvo.getPurordercode());// 采购订单编码
		// def26
		// itemVO.setAttributeValue("def13", bodyvo.getMny_scm());// 供应链金额（含税）
		// itemVO.setAttributeValue("def14", bodyvo.getMny_notax_scm());//
		// 供应链金额（不含税）
		// itemVO.setAttributeValue("def15", bodyvo.getTax_scm());// 供应链金额（税额）
		// itemVO.setAttributeValue("def21", bodyvo.getAccbodyid());// 后台接收id
		// itemVO.setAttributeValue("def23", bodyvo.getAccbillno());// 对账单号
		// itemVO.setAttributeValue("def24", bodyvo.getDeduction());// 扣款金额
		// itemVO.setAttributeValue("def25", bodyvo.getAppliedamount());// 申请金额
		/*
		 * itemVO.setAttributeValue("def22", bodyvo.getEvaluatecostbillno());//
		 * nc暂估工单编号 itemVO.setAttributeValue("def20", bodyvo.getMny_cost());//
		 * 成本 itemVO.setAttributeValue("def16", bodyvo.getMny_evaluatecost());//
		 * 暂估入成本
		 */}

	/**
	 * 设置主表信息
	 * 
	 * @param parentVO
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void setHeaderVO(PayableBillVO hvo, PayBillHeaderVO headvo)
			throws BusinessException {
		checkHeaderNotNull(headvo);

		/*
		 * String pk_deptid = getPk_DeptByCode(headvo.getDept(),
		 * hvo.getPk_org());// 部门 if (pk_deptid == null) { throw new
		 * BusinessException("部门【" + headvo.getDept() + "】未能在NC档案查询到相关信息"); }
		 * hvo.setAttributeValue("def46", pk_deptid);// 部门
		 * 
		 * String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc()); if
		 * (pk_psnodc == null) { throw new BusinessException("经办人【" +
		 * headvo.getPsndoc() + "】未能在NC档案查询到相关信息"); }
		 * hvo.setAttributeValue("def47", pk_psnodc);// 业务员
		 */
		hvo.setAttributeValue("def1", headvo.getEbsid());// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// 自定义项2
																// 外系统单据号->付款申请号
		hvo.setAttributeValue("def3", headvo.getImgno());// 自定义项3 影像编码->影像编码
		hvo.setAttributeValue("def4", headvo.getImgstate());// 自定义项4 影像状态->影像状态
		hvo.setAttributeValue("def5", headvo.getPurchaseno());// 采购协议编码
		hvo.setAttributeValue("def6", headvo.getPurchasename());// 采购协议名称
		hvo.setAttributeValue("def10", "SRM");// 自定义项10 来源外部系统
		hvo.setAttributeValue("def59", headvo.getAdvanceinv());// 预付款发票

		//hvo.setAttributeValue("def51", headvo.getReceivedamount());// 自定义项10
																	// 来源外部系统
		// hvo.setAttributeValue("def7", headvo.getConttype());// 自定义项7
		// 合同类型->合同类型
		// hvo.setAttributeValue("def8", headvo.getContcell());// 自定义项8
		// 合同细类->合同细类
		// hvo.setAttributeValue("def9", headvo.getEmergency());// 自定义项9
		// 紧急程度->紧急程度
		// hvo.setAttributeValue("def31", headvo.getNote());// 说明
		/*
		 * hvo.setAttributeValue("def35", headvo.getPurchaseno());// 采购协议编码
		 * hvo.setAttributeValue("def37", headvo.getPurchasename());// 采购协议名称
		 * hvo.setAttributeValue("def38", headvo.getAccbillno());// 对账单号
		 */
		String pk_supplier = getSupplierIDByCode(headvo.getSupplier(),
				hvo.getPk_org(), hvo.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("供应商【" + headvo.getSupplier()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// 供应商

		/*
		 * if (headvo.getProejctstages() != null) { String pk_proejctstages =
		 * getpk_projectByCode(headvo .getProejctstages()); if (pk_proejctstages
		 * == null) { throw new BusinessException("项目【" +
		 * headvo.getProejctstages() + "】未能在NC档案关联到相关信息!"); }
		 * hvo.setAttributeValue("def32", pk_proejctstages);// 自定义项32 项目分期 }
		 */
		hvo.setAttributeValue("def9", getdefdocBycode("zdy008", "02"));// 财务票据类型

	}

	/**
	 * 检验表体必录信息
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {

		/*
		 * if (StringUtils.isBlank(bodyvo.getChecktype())) { throw new
		 * BusinessException("票据类型（票种）不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_accadj())) { throw new
		 * BusinessException("对账调整（含税）不可为空"); } if
		 * (StringUtils.isBlank(bodyvo.getMny_notax_accadj())) { throw new
		 * BusinessException("对账调整（不含税）不可为空"); } if
		 * (StringUtils.isBlank(bodyvo.getTax_accadj())) { throw new
		 * BusinessException("对账调整（税额）不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_scm())) { throw new
		 * BusinessException("供应链金额（含税）不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_notax_scm())) { throw new
		 * BusinessException("供应链金额（不含税）不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getTax_scm())) { throw new
		 * BusinessException("供应链金额（税额）不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_evaluatecost())) { throw new
		 * BusinessException("暂估入成本不可为空"); }
		 */
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("供应商开票（含税）不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_notax_cr())) {
			throw new BusinessException("供应商开票（不含税）不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_tax_cr())) {
			throw new BusinessException("供应商开票（税额）不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getInvposted())) {
			throw new BusinessException("发票已入账标记不可为空");
		}
		/*
		 * if (StringUtils.isBlank(bodyvo.getMny_cost())) { throw new
		 * BusinessException("成本不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getAccbodyid())) { throw new
		 * BusinessException("后台接收id不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getEvaluatecostbillno())) { throw new
		 * BusinessException("nc暂估工单编号不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getAccbillno())) { throw new
		 * BusinessException("对账单号不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(bodyvo.getDeduction())) { throw new
		 * BusinessException("扣款金额不可为空"); } if
		 * (StringUtils.isBlank(bodyvo.getAppliedamount())) { throw new
		 * BusinessException("申请金额不可为空"); }
		 */
		// if (StringUtils.isBlank(bodyvo.getProejctdata())) {
		// throw new BusinessException("项目不可为空");
		// }
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(PayBillHeaderVO headvo)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("财务组织不可为空");
		}
		if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getDept())) { throw new
		 * BusinessException("经办部门不可为空"); }
		 */
		/*
		 * if (StringUtils.isBlank(headvo.getPsndoc())) { throw new
		 * BusinessException("经办人不可为空"); }
		 */

		if (!"Y".equals(headvo.getAdvanceinv())) {
			if (StringUtils.isBlank(headvo.getEbsid())) {
				throw new BusinessException("外系统主键不可为空");
			}
			if (StringUtils.isBlank(headvo.getEbsbillno())) {
				throw new BusinessException("外系统单据号不可为空");
			}
		}
//		if (StringUtils.isBlank(headvo.getImgno())) {
//			throw new BusinessException("影像编码不可为空");
//		}
//		if (StringUtils.isBlank(headvo.getImgstate())) {
//			throw new BusinessException("影像状态不可为空");
//		}
		if (StringUtils.isBlank(headvo.getAdvanceinv())) {
			throw new BusinessException("预付款发票不可为空");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getContcode())) { throw new
		 * BusinessException("合同编码不可为空"); } if
		 * (StringUtils.isBlank(headvo.getContname())) { throw new
		 * BusinessException("合同名称不可为空"); } if
		 * (StringUtils.isBlank(headvo.getConttype())) { throw new
		 * BusinessException("合同类型不可为空"); } if
		 * (StringUtils.isBlank(headvo.getContcell())) { throw new
		 * BusinessException("合同细类不可为空"); }
		 * 
		 * if (StringUtils.isBlank(headvo.getNote())) { throw new
		 * BusinessException("说明不可为空"); }
		 */

		if (StringUtils.isBlank(headvo.getPurchaseno())) {
			throw new BusinessException("采购协议编码不可为空");
		}
		if (StringUtils.isBlank(headvo.getPurchasename())) {
			throw new BusinessException("采购协议名称不可为空");
		}

	}

}
