package nc.bs.tg.checkfinance.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;

/**
 * 标准单据审核的BP
 */
public class AceCheckfinanceApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggCheckFinanceHVO[] approve(AggCheckFinanceHVO[] clientBills,
			AggCheckFinanceHVO[] originBills) {
		for (AggCheckFinanceHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggCheckFinanceHVO> update = new BillUpdate<AggCheckFinanceHVO>();
		AggCheckFinanceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
