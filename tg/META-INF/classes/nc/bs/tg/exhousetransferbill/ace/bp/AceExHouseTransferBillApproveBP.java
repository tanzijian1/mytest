package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

/**
 * 标准单据审核的BP
 */
public class AceExHouseTransferBillApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggExhousetransferbillHVO[] approve(AggExhousetransferbillHVO[] clientBills,
			AggExhousetransferbillHVO[] originBills) {
		for (AggExhousetransferbillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
			clientBill.getParentVO().setEffectdate(new UFDateTime());
		}
		BillUpdate<AggExhousetransferbillHVO> update = new BillUpdate<AggExhousetransferbillHVO>();
		AggExhousetransferbillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
