package nc.bs.tg.tg_groupdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceTG_GroupDataUnSendApproveBP {

	public AggGroupDataVO[] unSend(AggGroupDataVO[] clientBills,
			AggGroupDataVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggGroupDataVO> update = new BillUpdate<AggGroupDataVO>();
		AggGroupDataVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggGroupDataVO[] clientBills) {
		for (AggGroupDataVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
