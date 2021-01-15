package nc.bs.tg.checkfinance.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceCheckfinanceUnApproveBP {

	public AggCheckFinanceHVO[] unApprove(AggCheckFinanceHVO[] clientBills,
			AggCheckFinanceHVO[] originBills) {
		for (AggCheckFinanceHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggCheckFinanceHVO> update = new BillUpdate<AggCheckFinanceHVO>();
		AggCheckFinanceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
