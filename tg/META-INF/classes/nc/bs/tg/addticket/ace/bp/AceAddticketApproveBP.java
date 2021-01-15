package nc.bs.tg.addticket.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.addticket.AggAddTicket;

/**
 * ��׼������˵�BP
 */
public class AceAddticketApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggAddTicket[] approve(AggAddTicket[] clientBills,
			AggAddTicket[] originBills) {
		for (AggAddTicket clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggAddTicket> update = new BillUpdate<AggAddTicket>();
		AggAddTicket[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
