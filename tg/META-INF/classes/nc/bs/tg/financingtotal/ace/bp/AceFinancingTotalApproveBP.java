package nc.bs.tg.financingtotal.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.financingtotal.AggFinancingTotal;

/**
 * 标准单据审核的BP
 */
public class AceFinancingTotalApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggFinancingTotal[] approve(AggFinancingTotal[] clientBills,
			AggFinancingTotal[] originBills) {
		for (AggFinancingTotal clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFinancingTotal> update = new BillUpdate<AggFinancingTotal>();
		AggFinancingTotal[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
