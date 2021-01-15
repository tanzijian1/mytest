package nc.bs.tg.internalinterest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

/**
 * 标准单据审核的BP
 */
public class AceInternalInterestApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggInternalinterest[] approve(AggInternalinterest[] clientBills,
			AggInternalinterest[] originBills) {
		for (AggInternalinterest clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggInternalinterest> update = new BillUpdate<AggInternalinterest>();
		AggInternalinterest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
