package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceAbnormalTaxTransferUnApproveBP {

	public AggAbTaxTransferHVO[] unApprove(AggAbTaxTransferHVO[] clientBills,
			AggAbTaxTransferHVO[] originBills) {
		for (AggAbTaxTransferHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setBillstatus(-1);
		}
		BillUpdate<AggAbTaxTransferHVO> update = new BillUpdate<AggAbTaxTransferHVO>();
		AggAbTaxTransferHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
