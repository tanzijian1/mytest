package nc.bs.tg.paymentrequest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

/**
 * 标准单据审核的BP
 */
public class AcePaymentRequestApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggPayrequest[] approve(AggPayrequest[] clientBills,
			AggPayrequest[] originBills) {
		for (AggPayrequest clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggPayrequest> update = new BillUpdate<AggPayrequest>();
		AggPayrequest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
