package nc.bs.tg.costaccruebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceCostAccrueBillUnSendApproveBP {

	public AggCostAccrueBill[] unSend(AggCostAccrueBill[] clientBills,
			AggCostAccrueBill[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggCostAccrueBill> update = new BillUpdate<AggCostAccrueBill>();
		AggCostAccrueBill[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggCostAccrueBill[] clientBills) {
		for (AggCostAccrueBill clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
