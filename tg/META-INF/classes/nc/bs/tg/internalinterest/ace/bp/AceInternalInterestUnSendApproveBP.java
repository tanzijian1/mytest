package nc.bs.tg.internalinterest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceInternalInterestUnSendApproveBP {

	public AggInternalinterest[] unSend(AggInternalinterest[] clientBills,
			AggInternalinterest[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggInternalinterest> update = new BillUpdate<AggInternalinterest>();
		AggInternalinterest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggInternalinterest[] clientBills) {
		for (AggInternalinterest clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
