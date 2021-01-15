package nc.bs.tg.transferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;

/**
 * 标准单据弃审的BP
 */
public class AceTransferBillUnApproveBP {

	public AggTransferBillHVO[] unApprove(AggTransferBillHVO[] clientBills,
			AggTransferBillHVO[] originBills) {
		for (AggTransferBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setEffectdate(null);
		}
		BillUpdate<AggTransferBillHVO> update = new BillUpdate<AggTransferBillHVO>();
		AggTransferBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
