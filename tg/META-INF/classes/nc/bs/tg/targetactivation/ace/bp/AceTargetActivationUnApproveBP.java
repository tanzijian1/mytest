package nc.bs.tg.targetactivation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.vo.pub.VOStatus;

/**
 * ��׼���������BP
 */
public class AceTargetActivationUnApproveBP {

	public AggTargetactivation[] unApprove(AggTargetactivation[] clientBills,
			AggTargetactivation[] originBills) {
		for (AggTargetactivation clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggTargetactivation> update = new BillUpdate<AggTargetactivation>();
		AggTargetactivation[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
