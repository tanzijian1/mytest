package nc.bs.tg.distribution.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceDistributionUnApproveBP {

	public AggDistribution[] unApprove(AggDistribution[] clientBills,
			AggDistribution[] originBills) {
		for (AggDistribution clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggDistribution> update = new BillUpdate<AggDistribution>();
		AggDistribution[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
