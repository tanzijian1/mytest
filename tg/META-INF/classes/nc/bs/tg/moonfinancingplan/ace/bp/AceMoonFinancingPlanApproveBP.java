package nc.bs.tg.moonfinancingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;

/**
 * ��׼������˵�BP
 */
public class AceMoonFinancingPlanApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggMoonFinancingPlan[] approve(AggMoonFinancingPlan[] clientBills,
			AggMoonFinancingPlan[] originBills) {
		for (AggMoonFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMoonFinancingPlan> update = new BillUpdate<AggMoonFinancingPlan>();
		AggMoonFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
