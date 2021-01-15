package nc.bs.tg.addticket.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceFinancingExpenseUnSendApproveBP {

	public AggFinancexpenseVO[] unSend(AggFinancexpenseVO[] clientBills,
			AggFinancexpenseVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFinancexpenseVO> update = new BillUpdate<AggFinancexpenseVO>();
		AggFinancexpenseVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFinancexpenseVO[] clientBills) {
		for (AggFinancexpenseVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
