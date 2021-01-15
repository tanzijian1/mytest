package nc.bs.tg.changebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.changebill.AggChangeBillHVO;

/**
 * 标准单据弃审的BP
 */
public class AceChangeBillUnApproveBP {

	public AggChangeBillHVO[] unApprove(AggChangeBillHVO[] clientBills,
			AggChangeBillHVO[] originBills) {
		for (AggChangeBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setEffectdate(null);
		}
		BillUpdate<AggChangeBillHVO> update = new BillUpdate<AggChangeBillHVO>();
		AggChangeBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
