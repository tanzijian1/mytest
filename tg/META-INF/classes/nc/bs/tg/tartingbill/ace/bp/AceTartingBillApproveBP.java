package nc.bs.tg.tartingbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.tartingbill.AggTartingBillVO;

/**
 * ��׼������˵�BP
 */
public class AceTartingBillApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggTartingBillVO[] approve(AggTartingBillVO[] clientBills,
			AggTartingBillVO[] originBills) {
		for (AggTartingBillVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
		}
		BillUpdate<AggTartingBillVO> update = new BillUpdate<AggTartingBillVO>();
		AggTartingBillVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
