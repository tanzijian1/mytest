package nc.bs.tg.fischeme.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.fischeme.AggFIScemeHVO;

/**
 * 标准单据审核的BP
 */
public class AceFischemeApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggFIScemeHVO[] approve(AggFIScemeHVO[] clientBills,
			AggFIScemeHVO[] originBills) {
		for (AggFIScemeHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggFIScemeHVO> update = new BillUpdate<AggFIScemeHVO>();
		AggFIScemeHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
