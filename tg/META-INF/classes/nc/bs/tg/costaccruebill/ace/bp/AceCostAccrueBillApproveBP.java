package nc.bs.tg.costaccruebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;

/**
 * 标准单据审核的BP
 */
public class AceCostAccrueBillApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggCostAccrueBill[] approve(AggCostAccrueBill[] clientBills,
			AggCostAccrueBill[] originBills) {
		for (AggCostAccrueBill clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggCostAccrueBill> update = new BillUpdate<AggCostAccrueBill>();
		AggCostAccrueBill[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
