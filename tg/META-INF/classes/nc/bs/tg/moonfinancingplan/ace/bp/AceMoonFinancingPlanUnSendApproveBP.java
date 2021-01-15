package nc.bs.tg.moonfinancingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceMoonFinancingPlanUnSendApproveBP {

	public AggMoonFinancingPlan[] unSend(AggMoonFinancingPlan[] clientBills,
			AggMoonFinancingPlan[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggMoonFinancingPlan> update = new BillUpdate<AggMoonFinancingPlan>();
		AggMoonFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggMoonFinancingPlan[] clientBills) {
		for (AggMoonFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
