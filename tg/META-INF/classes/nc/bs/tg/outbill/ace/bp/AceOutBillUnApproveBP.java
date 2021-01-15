package nc.bs.tg.outbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceOutBillUnApproveBP {

	public AggOutbillHVO[] unApprove(AggOutbillHVO[] clientBills,
			AggOutbillHVO[] originBills) {
		for (AggOutbillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
		BillUpdate<AggOutbillHVO> update = new BillUpdate<AggOutbillHVO>();
		AggOutbillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
