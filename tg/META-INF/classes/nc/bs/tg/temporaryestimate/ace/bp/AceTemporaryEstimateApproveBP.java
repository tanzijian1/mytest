package nc.bs.tg.temporaryestimate.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.temporaryestimate.AggTemest;

/**
 * 标准单据审核的BP
 */
public class AceTemporaryEstimateApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggTemest[] approve(AggTemest[] clientBills,
			AggTemest[] originBills) {
		for (AggTemest clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggTemest> update = new BillUpdate<AggTemest>();
		AggTemest[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
