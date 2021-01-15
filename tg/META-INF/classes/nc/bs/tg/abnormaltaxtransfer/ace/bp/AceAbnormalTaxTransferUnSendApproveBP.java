package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceAbnormalTaxTransferUnSendApproveBP {

	public AggAbTaxTransferHVO[] unSend(AggAbTaxTransferHVO[] clientBills,
			AggAbTaxTransferHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggAbTaxTransferHVO> update = new BillUpdate<AggAbTaxTransferHVO>();
		AggAbTaxTransferHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggAbTaxTransferHVO[] clientBills) {
		for (AggAbTaxTransferHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setBillstatus(-1);
		}
	}
}
