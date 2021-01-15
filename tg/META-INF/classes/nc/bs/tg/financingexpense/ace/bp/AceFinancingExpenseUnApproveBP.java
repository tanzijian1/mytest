package nc.bs.tg.financingexpense.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

/**
 * 标准单据弃审的BP
 */
public class AceFinancingExpenseUnApproveBP {

	public AggFinancexpenseVO[] unApprove(AggFinancexpenseVO[] clientBills,
			AggFinancexpenseVO[] originBills) {
		for (AggFinancexpenseVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancexpenseVO> update = new BillUpdate<AggFinancexpenseVO>();
		AggFinancexpenseVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
