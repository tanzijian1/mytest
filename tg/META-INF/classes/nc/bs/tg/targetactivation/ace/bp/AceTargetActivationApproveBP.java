package nc.bs.tg.targetactivation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.targetactivation.AggTargetactivation;

/**
 * ��׼������˵�BP
 */
public class AceTargetActivationApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggTargetactivation[] approve(AggTargetactivation[] clientBills,
			AggTargetactivation[] originBills) {
		for (AggTargetactivation clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggTargetactivation> update = new BillUpdate<AggTargetactivation>();
		AggTargetactivation[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
