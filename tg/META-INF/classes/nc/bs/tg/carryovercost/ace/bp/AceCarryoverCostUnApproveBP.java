package nc.bs.tg.carryovercost.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceCarryoverCostUnApproveBP {

	public AggCarrycost[] unApprove(AggCarrycost[] clientBills,
			AggCarrycost[] originBills) {
		for (AggCarrycost clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggCarrycost> update = new BillUpdate<AggCarrycost>();
		AggCarrycost[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
