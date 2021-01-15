package nc.bs.tg.carryovercost.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.carryovercost.AggCarrycost;

/**
 * ��׼������˵�BP
 */
public class AceCarryoverCostApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggCarrycost[] approve(AggCarrycost[] clientBills,
			AggCarrycost[] originBills) {
		for (AggCarrycost clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggCarrycost> update = new BillUpdate<AggCarrycost>();
		AggCarrycost[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
