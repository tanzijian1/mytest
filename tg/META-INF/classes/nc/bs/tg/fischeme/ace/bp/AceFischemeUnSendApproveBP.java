package nc.bs.tg.fischeme.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceFischemeUnSendApproveBP {

	public AggFIScemeHVO[] unSend(AggFIScemeHVO[] clientBills,
			AggFIScemeHVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFIScemeHVO> update = new BillUpdate<AggFIScemeHVO>();
		AggFIScemeHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFIScemeHVO[] clientBills) {
		for (AggFIScemeHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
