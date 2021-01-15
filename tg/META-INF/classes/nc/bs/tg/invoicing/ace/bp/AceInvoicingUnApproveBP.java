package nc.bs.tg.invoicing.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInvoicingUnApproveBP {

	public AggInvoicingHead[] unApprove(AggInvoicingHead[] clientBills,
			AggInvoicingHead[] originBills) {
		for (AggInvoicingHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInvoicingHead> update = new BillUpdate<AggInvoicingHead>();
		AggInvoicingHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
