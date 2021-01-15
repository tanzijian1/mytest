package nc.bs.tg.fischeme.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceFischemeUnApproveBP {

	public AggFIScemeHVO[] unApprove(AggFIScemeHVO[] clientBills,
			AggFIScemeHVO[] originBills) {
		for (AggFIScemeHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFIScemeHVO> update = new BillUpdate<AggFIScemeHVO>();
		AggFIScemeHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
