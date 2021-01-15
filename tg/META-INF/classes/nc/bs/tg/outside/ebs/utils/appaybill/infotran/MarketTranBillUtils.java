package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import org.apache.commons.lang.StringUtils;

import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

/**
 * 营销费请款
 * 
 * @author ASUS
 * 
 */
public class MarketTranBillUtils extends CostTranBillUtils {
	static MarketTranBillUtils utils;

	public static MarketTranBillUtils getUtils() {
		if (utils == null) {
			utils = new MarketTranBillUtils();
		}
		return utils;
	}

	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			PayBillItemVO bodyvo) throws BusinessException {
		setItemVO(headVO, itemVO, bodyvo);
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
		super.setHeaderVO(hvo, headvo);
		String pk_deptid = getPk_DeptByCode(headvo.getDept(), hvo.getPk_org());// 部门
		if (pk_deptid == null) {
			throw new BusinessException("部门【" + headvo.getDept()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_DEPTID, pk_deptid);// 部门

		String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc());
		if (pk_psnodc == null) {
			throw new BusinessException("经办人【" + headvo.getPsndoc()
					+ "】未能在NC档案查询到相关信息");
		}
		hvo.setAttributeValue(PayableBillVO.PK_PSNDOC, pk_psnodc);// 业务员

		hvo.setAttributeValue("def9", getdefdocBycode("02", "zdy008"));// 财务票据类型

	}

}
