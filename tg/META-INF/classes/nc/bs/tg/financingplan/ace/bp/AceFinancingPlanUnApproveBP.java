package nc.bs.tg.financingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceFinancingPlanUnApproveBP {

	public AggFinancingPlan[] unApprove(AggFinancingPlan[] clientBills,
			AggFinancingPlan[] originBills) {
		for (AggFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancingPlan> update = new BillUpdate<AggFinancingPlan>();
		AggFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
