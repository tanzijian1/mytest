package nc.bs.tg.rzreportbi.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;

/**
 * ��׼������˵�BP
 */
public class AceRZreportBIApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggRZreportBIVO[] approve(AggRZreportBIVO[] clientBills,
			AggRZreportBIVO[] originBills) {
		for (AggRZreportBIVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggRZreportBIVO> update = new BillUpdate<AggRZreportBIVO>();
		AggRZreportBIVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
