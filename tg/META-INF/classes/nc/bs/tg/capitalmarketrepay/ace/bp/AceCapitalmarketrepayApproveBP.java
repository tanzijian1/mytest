package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

/**
 * 标准单据审核的BP
 */
public class AceCapitalmarketrepayApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggMarketRepalayVO[] approve(AggMarketRepalayVO[] clientBills,
			AggMarketRepalayVO[] originBills) {
		for (AggMarketRepalayVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMarketRepalayVO> update = new BillUpdate<AggMarketRepalayVO>();
		AggMarketRepalayVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
