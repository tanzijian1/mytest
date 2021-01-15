package nc.bs.tg.paymentrequest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AcePaymentRequestUnApproveBP {

	public AggPayrequest[] unApprove(AggPayrequest[] clientBills,
			AggPayrequest[] originBills) {
		for (AggPayrequest clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggPayrequest> update = new BillUpdate<AggPayrequest>();
		AggPayrequest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
