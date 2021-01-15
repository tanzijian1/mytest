package nc.bs.tg.financingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.financingplan.AggFinancingPlan;

/**
 * ��׼������˵�BP
 */
public class AceFinancingPlanApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggFinancingPlan[] approve(AggFinancingPlan[] clientBills,
			AggFinancingPlan[] originBills) {
		for (AggFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancingPlan> update = new BillUpdate<AggFinancingPlan>();
		AggFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
