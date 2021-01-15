package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceCapitalmarketrepayUnApproveBP {

	public AggMarketRepalayVO[] unApprove(AggMarketRepalayVO[] clientBills,
			AggMarketRepalayVO[] originBills) {
		for (AggMarketRepalayVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMarketRepalayVO> update = new BillUpdate<AggMarketRepalayVO>();
		AggMarketRepalayVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
