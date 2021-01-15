package nc.bs.tg.interestshare.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInterestShareUnApproveBP {

	public AggIntshareHead[] unApprove(AggIntshareHead[] clientBills,
			AggIntshareHead[] originBills) {
		for (AggIntshareHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggIntshareHead> update = new BillUpdate<AggIntshareHead>();
		AggIntshareHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
