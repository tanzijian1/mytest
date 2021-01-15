package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceSalaryfundaccureUnApproveBP {

	public AggSalaryfundaccure[] unApprove(AggSalaryfundaccure[] clientBills,
			AggSalaryfundaccure[] originBills) {
		for (AggSalaryfundaccure clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSalaryfundaccure> update = new BillUpdate<AggSalaryfundaccure>();
		AggSalaryfundaccure[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
