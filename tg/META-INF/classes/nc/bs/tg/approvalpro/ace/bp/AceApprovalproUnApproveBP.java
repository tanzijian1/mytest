package nc.bs.tg.approvalpro.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceApprovalproUnApproveBP {

	public AggApprovalProVO[] unApprove(AggApprovalProVO[] clientBills,
			AggApprovalProVO[] originBills) {
		for (AggApprovalProVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggApprovalProVO> update = new BillUpdate<AggApprovalProVO>();
		AggApprovalProVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
