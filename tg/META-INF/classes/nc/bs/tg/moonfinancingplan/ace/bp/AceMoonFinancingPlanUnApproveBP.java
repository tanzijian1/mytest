package nc.bs.tg.moonfinancingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceMoonFinancingPlanUnApproveBP {

	public AggMoonFinancingPlan[] unApprove(AggMoonFinancingPlan[] clientBills,
			AggMoonFinancingPlan[] originBills) {
		for (AggMoonFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMoonFinancingPlan> update = new BillUpdate<AggMoonFinancingPlan>();
		AggMoonFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
