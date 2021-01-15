package nc.bs.tg.marginsheet.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.marginsheet.AggMarginHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceMarginSheetUnApproveBP {

	public AggMarginHVO[] unApprove(AggMarginHVO[] clientBills,
			AggMarginHVO[] originBills) {
		for (AggMarginHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMarginHVO> update = new BillUpdate<AggMarginHVO>();
		AggMarginHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
