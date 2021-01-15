package nc.bs.tg.tartingbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceTartingBillUnSendApproveBP {

	public AggTartingBillVO[] unSend(AggTartingBillVO[] clientBills,
			AggTartingBillVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggTartingBillVO> update = new BillUpdate<AggTartingBillVO>();
		AggTartingBillVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggTartingBillVO[] clientBills) {
		for (AggTartingBillVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(0);
		}
	}
}
