package nc.bs.tg.interestshare.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceInterestShareUnSendApproveBP {

	public AggIntshareHead[] unSend(AggIntshareHead[] clientBills,
			AggIntshareHead[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggIntshareHead> update = new BillUpdate<AggIntshareHead>();
		AggIntshareHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggIntshareHead[] clientBills) {
		for (AggIntshareHead clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
