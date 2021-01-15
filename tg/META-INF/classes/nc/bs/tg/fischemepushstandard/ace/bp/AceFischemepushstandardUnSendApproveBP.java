package nc.bs.tg.fischemepushstandard.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceFischemepushstandardUnSendApproveBP {

	public AggFischemePushStandardHVO[] unSend(AggFischemePushStandardHVO[] clientBills,
			AggFischemePushStandardHVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggFischemePushStandardHVO> update = new BillUpdate<AggFischemePushStandardHVO>();
		AggFischemePushStandardHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggFischemePushStandardHVO[] clientBills) {
		for (AggFischemePushStandardHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
