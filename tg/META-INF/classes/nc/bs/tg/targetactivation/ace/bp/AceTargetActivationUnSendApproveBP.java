package nc.bs.tg.targetactivation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceTargetActivationUnSendApproveBP {

	public AggTargetactivation[] unSend(AggTargetactivation[] clientBills,
			AggTargetactivation[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggTargetactivation> update = new BillUpdate<AggTargetactivation>();
		AggTargetactivation[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggTargetactivation[] clientBills) {
		for (AggTargetactivation clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
