package nc.bs.tg.paymentrequest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AcePaymentRequestUnSendApproveBP {

	public AggPayrequest[] unSend(AggPayrequest[] clientBills,
			AggPayrequest[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggPayrequest> update = new BillUpdate<AggPayrequest>();
		AggPayrequest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggPayrequest[] clientBills) {
		for (AggPayrequest clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
