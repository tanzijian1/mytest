package nc.bs.tg.outbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.outbill.AggOutbillHVO;

/**
 * 标准单据审核的BP
 */
public class AceOutBillApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggOutbillHVO[] approve(AggOutbillHVO[] clientBills,
			AggOutbillHVO[] originBills) {
		for (AggOutbillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggOutbillHVO> update = new BillUpdate<AggOutbillHVO>();
		AggOutbillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
