package nc.bs.tg.internalinterest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInternalInterestUnApproveBP {

	public AggInternalinterest[] unApprove(AggInternalinterest[] clientBills,
			AggInternalinterest[] originBills) {
		for (AggInternalinterest clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggInternalinterest> update = new BillUpdate<AggInternalinterest>();
		AggInternalinterest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
