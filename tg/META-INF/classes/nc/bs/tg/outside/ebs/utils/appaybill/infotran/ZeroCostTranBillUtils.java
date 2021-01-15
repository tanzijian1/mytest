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

/**
 * 成本零元应付单
 * 
 * @author ASUS
 * 
 */
public class ZeroCostTranBillUtils extends CostTranBillUtils {
	static ZeroCostTranBillUtils utils;

	public static ZeroCostTranBillUtils getUtils() {
		if (utils == null) {
			utils = new ZeroCostTranBillUtils();
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
		setHeaderVO(hvo, headvo);
	}

}
