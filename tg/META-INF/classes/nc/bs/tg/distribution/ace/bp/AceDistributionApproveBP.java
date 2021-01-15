package nc.bs.tg.distribution.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.distribution.AggDistribution;

/**
 * ��׼������˵�BP
 */
public class AceDistributionApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggDistribution[] approve(AggDistribution[] clientBills,
			AggDistribution[] originBills) {
		for (AggDistribution clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggDistribution> update = new BillUpdate<AggDistribution>();
		AggDistribution[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
