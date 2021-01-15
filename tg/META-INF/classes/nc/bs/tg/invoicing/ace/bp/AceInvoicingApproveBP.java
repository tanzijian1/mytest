package nc.bs.tg.invoicing.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

/**
 * ��׼������˵�BP
 */
public class AceInvoicingApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggInvoicingHead[] approve(AggInvoicingHead[] clientBills,
			AggInvoicingHead[] originBills) {
		for (AggInvoicingHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggInvoicingHead> update = new BillUpdate<AggInvoicingHead>();
		AggInvoicingHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
