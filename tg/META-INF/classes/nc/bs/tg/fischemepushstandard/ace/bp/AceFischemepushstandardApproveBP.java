package nc.bs.tg.fischemepushstandard.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;

/**
 * ��׼������˵�BP
 */
public class AceFischemepushstandardApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggFischemePushStandardHVO[] approve(AggFischemePushStandardHVO[] clientBills,
			AggFischemePushStandardHVO[] originBills) {
		for (AggFischemePushStandardHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFischemePushStandardHVO> update = new BillUpdate<AggFischemePushStandardHVO>();
		AggFischemePushStandardHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
