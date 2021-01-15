package nc.bs.tg.approvalpro.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.approvalpro.AggApprovalProVO;

/**
 * 标准单据审核的BP
 */
public class AceApprovalproApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggApprovalProVO[] approve(AggApprovalProVO[] clientBills,
			AggApprovalProVO[] originBills) {
		for (AggApprovalProVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggApprovalProVO> update = new BillUpdate<AggApprovalProVO>();
		AggApprovalProVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
