package nc.bs.tg.renamechangebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceRenameChangeBillUnSendApproveBP {

	public AggRenameChangeBillHVO[] unSend(AggRenameChangeBillHVO[] clientBills,
			AggRenameChangeBillHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggRenameChangeBillHVO> update = new BillUpdate<AggRenameChangeBillHVO>();
		AggRenameChangeBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggRenameChangeBillHVO[] clientBills) {
		for (AggRenameChangeBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
