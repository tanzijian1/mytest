package nc.bs.tg.financingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceFinancingPlanUnSendApproveBP {

	public AggFinancingPlan[] unSend(AggFinancingPlan[] clientBills,
			AggFinancingPlan[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFinancingPlan> update = new BillUpdate<AggFinancingPlan>();
		AggFinancingPlan[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFinancingPlan[] clientBills) {
		for (AggFinancingPlan clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
