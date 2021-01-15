package nc.bs.tg.interestshare.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.interestshare.AggIntshareHead;

/**
 * 标准单据审核的BP
 */
public class AceInterestShareApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggIntshareHead[] approve(AggIntshareHead[] clientBills,
			AggIntshareHead[] originBills) {
		for (AggIntshareHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggIntshareHead> update = new BillUpdate<AggIntshareHead>();
		AggIntshareHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
