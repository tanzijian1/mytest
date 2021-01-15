package nc.bs.tg.fischeme.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceFischemeUnSendApproveBP {

	public AggFIScemeHVO[] unSend(AggFIScemeHVO[] clientBills,
			AggFIScemeHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFIScemeHVO> update = new BillUpdate<AggFIScemeHVO>();
		AggFIScemeHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFIScemeHVO[] clientBills) {
		for (AggFIScemeHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
