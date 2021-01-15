package nc.bs.tg.invoicebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceInvoicebillUnApproveBP {

	public AggInvoiceBillVO[] unApprove(AggInvoiceBillVO[] clientBills,
			AggInvoiceBillVO[] originBills) {
		for (AggInvoiceBillVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggInvoiceBillVO> update = new BillUpdate<AggInvoiceBillVO>();
		AggInvoiceBillVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
