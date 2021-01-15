package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceSalaryfundaccureUnSendApproveBP {

	public AggSalaryfundaccure[] unSend(AggSalaryfundaccure[] clientBills,
			AggSalaryfundaccure[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggSalaryfundaccure> update = new BillUpdate<AggSalaryfundaccure>();
		AggSalaryfundaccure[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggSalaryfundaccure[] clientBills) {
		for (AggSalaryfundaccure clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
