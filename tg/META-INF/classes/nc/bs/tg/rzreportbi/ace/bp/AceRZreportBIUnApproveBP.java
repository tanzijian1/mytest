package nc.bs.tg.rzreportbi.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceRZreportBIUnApproveBP {

	public AggRZreportBIVO[] unApprove(AggRZreportBIVO[] clientBills,
			AggRZreportBIVO[] originBills) {
		for (AggRZreportBIVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggRZreportBIVO> update = new BillUpdate<AggRZreportBIVO>();
		AggRZreportBIVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
