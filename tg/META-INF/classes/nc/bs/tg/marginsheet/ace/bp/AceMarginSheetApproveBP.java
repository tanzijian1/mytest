package nc.bs.tg.marginsheet.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.marginsheet.AggMarginHVO;

/**
 * 标准单据审核的BP
 */
public class AceMarginSheetApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggMarginHVO[] approve(AggMarginHVO[] clientBills,
			AggMarginHVO[] originBills) {
		for (AggMarginHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMarginHVO> update = new BillUpdate<AggMarginHVO>();
		AggMarginHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
