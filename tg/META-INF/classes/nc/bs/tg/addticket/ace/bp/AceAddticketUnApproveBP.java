package nc.bs.tg.addticket.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceAddticketUnApproveBP {

	public AggAddTicket[] unApprove(AggAddTicket[] clientBills,
			AggAddTicket[] originBills) {
		for (AggAddTicket clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggAddTicket> update = new BillUpdate<AggAddTicket>();
		AggAddTicket[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
