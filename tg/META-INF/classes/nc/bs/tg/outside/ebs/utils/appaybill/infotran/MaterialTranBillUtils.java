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
 * 材料请款单
 * 
 * @author ASUS
 * 
 */
public class MaterialTranBillUtils extends BillTranUtils {
	static MaterialTranBillUtils utils;

	public static MaterialTranBillUtils getUtils() {
		if (utils == null) {
			utils = new MaterialTranBillUtils();
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

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, BillEnumCollection.ObjType.SUPPLIER.VALUE);// 往来对象

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

/*		itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, bodyvo.getSubjcode());// 会计科目
		itemVO.setAttributeValue(PayableBillItemVO.PK_SUBJCODE, bodyvo.getInoutbusiclass());// 收支科目
		itemVO.setAttributeValue(PayableBillItemVO.DEF2, bodyvo.getPaymenttype());// 款项类型
*/		String pk_supplier = getSupplierIDByCode(bodyvo.getSupplier(),
				headVO.getPk_org(), headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("实际收款方【" + bodyvo.getSupplier()
					+ "】未能在NC档案查询到相关信息");
		}
		itemVO.setAttributeValue(PayableBillItemVO.SUPPLIER, pk_supplier);// 供应商

		// 票种
		if (bodyvo.getInvtype() != null) {
			String invtype = getdefdocBycode(
					bodyvo.getInvtype(),"pjlx");
			if (invtype == null) {
				throw new BusinessException("票种[" + bodyvo.getInvtype()
						+ "]未能在NC档案关联到!");
			}
			itemVO.setAttributeValue("def8", invtype);//
		}

	}
	

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

		String pk_deptid = getPk_DeptByCode(headvo.getDept(), hvo.getPk_org());// 部门
		if (pk_deptid == null) {
			throw new BusinessException("部门【" + headvo.getDept()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue("def46", pk_deptid);// 部门

		String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc());
		if (pk_psnodc == null) {
			throw new BusinessException("经办人【" + headvo.getPsndoc()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue("def47", pk_psnodc);// 业务员
/*
		String pk_balatype = getBalatypePkByCode(headvo.getBalatype());
		if (pk_balatype == null) {
			throw new BusinessException("结算方式【" + headvo.getBalatype()
					+ "】未能在NC档案查询到相关信息!");
		}
		hvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// 结算方式
*/
		hvo.setAttributeValue("def1", headvo.getEbsid());// 自定义项1 外系统主键
		hvo.setAttributeValue("def2", headvo.getEbsbillno());// 自定义项2
																// 外系统单据号->付款申请号
		/*hvo.setAttributeValue("def3", headvo.getImgno());// 自定义项3 影像编码->影像编码
		hvo.setAttributeValue("def4", headvo.getImgstate());// 自定义项4 影像状态->影像状态
*/		hvo.setAttributeValue("def5", headvo.getContcode());// 自定义项5 合同编码->合同编码
		//hvo.setAttributeValue("def6", headvo.getContname());// 合同名称->合同名称 def6
		/*hvo.setAttributeValue("def7", headvo.getConttype());// 自定义项7
		// 合同类型->合同类型
		hvo.setAttributeValue("def8", headvo.getContcell());// 自定义项8 合同细类->合同细类 
*/		// hvo.setAttributeValue("def9", headvo.getEmergency());// 自定义项9
		// 紧急程度->紧急程度
		if (StringUtils.isNotBlank(headvo.getProejctdata())) {
			String pk_projectdata = getpk_projectByCode(headvo.getProejctdata());
			if (pk_projectdata == null) {
				throw new BusinessException("项目【" + headvo.getProejctdata()
						+ "】未能在NC档案关联到相关信息!");
			}
			hvo.setAttributeValue("def19", pk_projectdata);// 项目
		}
		
		hvo.setAttributeValue("def31", headvo.getNote());//说明
		hvo.setAttributeValue("def10", "SRM");// 自定义项10 来源外部系统
		hvo.setAttributeValue("def9", getdefdocBycode( "02","zdy008"));// 财务票据类型

	}

	/**
	 * 检验表体必录信息
	 *  
	 * @param bodyvo
	 * @throws BusinessException
	 */
	protected void checkItemNotNull(PayBillItemVO bodyvo)
			throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("供应商不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getTaxrate())) {
			throw new BusinessException("税率不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getLocal_money_cr())) {
			throw new BusinessException("价税合计不可为空");
		}
		if (StringUtils.isBlank(bodyvo.getInvtype())) {
			throw new BusinessException("票据类型（票种）不可为空");
		}
		
	}

	/**
	 * 检验表头必录信息
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(PayBillHeaderVO headvo)
			throws BusinessException {
		if (headvo.getDept() == null) {
			throw new BusinessException("经办部门信息不可为空!");
		}
		if (headvo.getPsndoc() == null) {
			throw new BusinessException("经办人信息不可为空!");
		}
/*
		if (headvo.getBalatype() == null) {
			throw new BusinessException("结算方式不可为空!");
		}*/

		if (headvo.getEbsid() == null) {
			throw new BusinessException("EBS主键不可为空");
		}

		if (headvo.getEbsbillno() == null) {
			throw new BusinessException("EBS编码不可为空");
		}
/*		if (headvo.getImgno() == null) {
			throw new BusinessException("影像主键不可为空");
		}
		if (headvo.getImgstate() == null) {
			throw new BusinessException("影像状态不可为空");
		}*/
		if (headvo.getContcode() == null) {
			throw new BusinessException("合同编码不可为空");
		}
		/*if (headvo.getContname() == null) {
			throw new BusinessException("合同名称不可为空");
		}*/
/*		if (headvo.getConttype() == null) {
			throw new BusinessException("合同类型不可为空");
		}
		if (headvo.getContcell() == null) {
			throw new BusinessException("合同细类不可为空");
		}*/
		if (headvo.getProejctdata() == null) {
			throw new BusinessException("项目不可为空");
		}
	/*	if (StringUtils.isBlank(headvo.getSupplier())) {
			throw new BusinessException("收款方不可为空");
		}*/
/*		if (headvo.getNote() == null) {
			throw new BusinessException("说明不可为空");
		}*/
	}
}
