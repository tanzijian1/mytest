package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;

/**
 * 标准单据审核的BP
 */
public class AceSalaryfundaccureApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggSalaryfundaccure[] approve(AggSalaryfundaccure[] clientBills,
			AggSalaryfundaccure[] originBills) {
		for (AggSalaryfundaccure clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSalaryfundaccure> update = new BillUpdate<AggSalaryfundaccure>();
		AggSalaryfundaccure[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
