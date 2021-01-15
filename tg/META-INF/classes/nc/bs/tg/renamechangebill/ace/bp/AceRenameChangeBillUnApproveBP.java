package nc.bs.tg.renamechangebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

/**
 * 标准单据弃审的BP
 */
public class AceRenameChangeBillUnApproveBP {

	public AggRenameChangeBillHVO[] unApprove(AggRenameChangeBillHVO[] clientBills,
			AggRenameChangeBillHVO[] originBills) {
		for (AggRenameChangeBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setEffectdate(null);
		}
		BillUpdate<AggRenameChangeBillHVO> update = new BillUpdate<AggRenameChangeBillHVO>();
		AggRenameChangeBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
