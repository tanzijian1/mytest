package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import org.apache.commons.lang.StringUtils;

import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.appaybill.PayBillHeaderVO;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

/**
 * Ӫ�������
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
	 * ����������Ϣ
	 * 
	 * @param parentVO
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void setHeaderVO(PayableBillVO hvo, PayBillHeaderVO headvo)
			throws BusinessException {
		super.setHeaderVO(hvo, headvo);
		String pk_deptid = getPk_DeptByCode(headvo.getDept(), hvo.getPk_org());// ����
		if (pk_deptid == null) {
			throw new BusinessException("���š�" + headvo.getDept()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue(PayableBillVO.PK_DEPTID, pk_deptid);// ����

		String pk_psnodc = getPsndocPkByCode(headvo.getPsndoc());
		if (pk_psnodc == null) {
			throw new BusinessException("�����ˡ�" + headvo.getPsndoc()
					+ "��δ����NC������ѯ�������Ϣ");
		}
		hvo.setAttributeValue(PayableBillVO.PK_PSNDOC, pk_psnodc);// ҵ��Ա

		hvo.setAttributeValue("def9", getdefdocBycode("02", "zdy008"));// ����Ʊ������

	}

}
