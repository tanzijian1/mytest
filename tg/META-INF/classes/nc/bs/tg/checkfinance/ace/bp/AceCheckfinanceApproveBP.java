package nc.bs.tg.checkfinance.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;

/**
 * ��׼������˵�BP
 */
public class AceCheckfinanceApproveBP {

	/**
	 * ��˶���
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
