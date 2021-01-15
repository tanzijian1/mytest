package nc.bs.tg.agoodsdetail.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceAGoodsDetailUnSendApproveBP {

	public AggAGoodsDetail[] unSend(AggAGoodsDetail[] clientBills,
			AggAGoodsDetail[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggAGoodsDetail> update = new BillUpdate<AggAGoodsDetail>();
		AggAGoodsDetail[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggAGoodsDetail[] clientBills) {
		for (AggAGoodsDetail clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
