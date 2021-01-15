package nc.bs.tg.financingtotal.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceFinancingTotalUnApproveBP {

	public AggFinancingTotal[] unApprove(AggFinancingTotal[] clientBills,
			AggFinancingTotal[] originBills) {
		for (AggFinancingTotal clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancingTotal> update = new BillUpdate<AggFinancingTotal>();
		AggFinancingTotal[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
