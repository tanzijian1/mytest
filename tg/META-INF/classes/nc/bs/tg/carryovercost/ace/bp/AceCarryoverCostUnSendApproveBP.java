package nc.bs.tg.carryovercost.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceCarryoverCostUnSendApproveBP {

	public AggCarrycost[] unSend(AggCarrycost[] clientBills,
			AggCarrycost[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggCarrycost> update = new BillUpdate<AggCarrycost>();
		AggCarrycost[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggCarrycost[] clientBills) {
		for (AggCarrycost clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
