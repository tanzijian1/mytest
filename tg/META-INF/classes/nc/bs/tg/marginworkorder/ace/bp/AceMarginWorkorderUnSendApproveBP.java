package nc.bs.tg.marginworkorder.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceMarginWorkorderUnSendApproveBP {

	public AggMarginHVO[] unSend(AggMarginHVO[] clientBills,
			AggMarginHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggMarginHVO> update = new BillUpdate<AggMarginHVO>();
		AggMarginHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggMarginHVO[] clientBills) {
		for (AggMarginHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
