package nc.bs.tg.invoicebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;

/**
 * 标准单据审核的BP
 */
public class AceInvoicebillApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggInvoiceBillVO[] approve(AggInvoiceBillVO[] clientBills,
			AggInvoiceBillVO[] originBills) {
		for (AggInvoiceBillVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggInvoiceBillVO> update = new BillUpdate<AggInvoiceBillVO>();
		AggInvoiceBillVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
