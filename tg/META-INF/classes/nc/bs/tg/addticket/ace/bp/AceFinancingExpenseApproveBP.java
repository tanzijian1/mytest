package nc.bs.tg.addticket.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

/**
 * 标准单据审核的BP
 */
public class AceFinancingExpenseApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggFinancexpenseVO[] approve(AggFinancexpenseVO[] clientBills,
			AggFinancexpenseVO[] originBills) {
		for (AggFinancexpenseVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancexpenseVO> update = new BillUpdate<AggFinancexpenseVO>();
		AggFinancexpenseVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
