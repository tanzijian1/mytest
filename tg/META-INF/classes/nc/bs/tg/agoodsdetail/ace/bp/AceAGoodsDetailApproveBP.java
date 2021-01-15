package nc.bs.tg.agoodsdetail.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;

/**
 * ��׼������˵�BP
 */
public class AceAGoodsDetailApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggAGoodsDetail[] approve(AggAGoodsDetail[] clientBills,
			AggAGoodsDetail[] originBills) {
		for (AggAGoodsDetail clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggAGoodsDetail> update = new BillUpdate<AggAGoodsDetail>();
		AggAGoodsDetail[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
