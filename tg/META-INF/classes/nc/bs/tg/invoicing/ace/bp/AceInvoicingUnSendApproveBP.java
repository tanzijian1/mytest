package nc.bs.tg.invoicing.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceInvoicingUnSendApproveBP {

	public AggInvoicingHead[] unSend(AggInvoicingHead[] clientBills,
			AggInvoicingHead[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInvoicingHead> update = new BillUpdate<AggInvoicingHead>();
		AggInvoicingHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInvoicingHead[] clientBills) {
		for (AggInvoicingHead clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
