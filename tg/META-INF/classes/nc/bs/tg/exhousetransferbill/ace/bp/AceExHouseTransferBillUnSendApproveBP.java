package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceExHouseTransferBillUnSendApproveBP {

	public AggExhousetransferbillHVO[] unSend(AggExhousetransferbillHVO[] clientBills,
			AggExhousetransferbillHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggExhousetransferbillHVO> update = new BillUpdate<AggExhousetransferbillHVO>();
		AggExhousetransferbillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggExhousetransferbillHVO[] clientBills) {
		for (AggExhousetransferbillHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
