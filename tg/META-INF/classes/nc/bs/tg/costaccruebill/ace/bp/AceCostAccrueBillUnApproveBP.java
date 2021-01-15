package nc.bs.tg.costaccruebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceCostAccrueBillUnApproveBP {

	public AggCostAccrueBill[] unApprove(AggCostAccrueBill[] clientBills,
			AggCostAccrueBill[] originBills) {
		for (AggCostAccrueBill clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggCostAccrueBill> update = new BillUpdate<AggCostAccrueBill>();
		AggCostAccrueBill[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
