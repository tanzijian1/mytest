package nc.bs.tg.fischemepushstandard.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceFischemepushstandardUnApproveBP {

	public AggFischemePushStandardHVO[] unApprove(AggFischemePushStandardHVO[] clientBills,
			AggFischemePushStandardHVO[] originBills) {
		for (AggFischemePushStandardHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFischemePushStandardHVO> update = new BillUpdate<AggFischemePushStandardHVO>();
		AggFischemePushStandardHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
