package nc.bs.tg.invoicebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceInvoicebillUnSendApproveBP {

	public AggInvoiceBillVO[] unSend(AggInvoiceBillVO[] clientBills,
			AggInvoiceBillVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInvoiceBillVO> update = new BillUpdate<AggInvoiceBillVO>();
		AggInvoiceBillVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInvoiceBillVO[] clientBills) {
		for (AggInvoiceBillVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
