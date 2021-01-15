package nc.bs.tg.temporaryestimate.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceTemporaryEstimateUnSendApproveBP {

	public AggTemest[] unSend(AggTemest[] clientBills,
			AggTemest[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggTemest> update = new BillUpdate<AggTemest>();
		AggTemest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggTemest[] clientBills) {
		for (AggTemest clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
