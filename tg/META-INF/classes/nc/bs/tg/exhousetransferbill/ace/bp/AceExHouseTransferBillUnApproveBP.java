package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

/**
 * 标准单据弃审的BP
 */
public class AceExHouseTransferBillUnApproveBP {

	public AggExhousetransferbillHVO[] unApprove(AggExhousetransferbillHVO[] clientBills,
			AggExhousetransferbillHVO[] originBills) {
		for (AggExhousetransferbillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
			clientBill.getParentVO().setEffectdate(null);
		}
		BillUpdate<AggExhousetransferbillHVO> update = new BillUpdate<AggExhousetransferbillHVO>();
		AggExhousetransferbillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
