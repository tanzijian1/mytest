package nc.bs.tg.agoodsdetail.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceAGoodsDetailUnApproveBP {

	public AggAGoodsDetail[] unApprove(AggAGoodsDetail[] clientBills,
			AggAGoodsDetail[] originBills) {
		for (AggAGoodsDetail clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggAGoodsDetail> update = new BillUpdate<AggAGoodsDetail>();
		AggAGoodsDetail[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
