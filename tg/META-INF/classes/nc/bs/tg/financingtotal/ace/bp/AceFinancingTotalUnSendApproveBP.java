package nc.bs.tg.financingtotal.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceFinancingTotalUnSendApproveBP {

	public AggFinancingTotal[] unSend(AggFinancingTotal[] clientBills,
			AggFinancingTotal[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFinancingTotal> update = new BillUpdate<AggFinancingTotal>();
		AggFinancingTotal[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFinancingTotal[] clientBills) {
		for (AggFinancingTotal clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
