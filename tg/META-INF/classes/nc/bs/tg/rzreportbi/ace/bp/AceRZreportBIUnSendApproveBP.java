package nc.bs.tg.rzreportbi.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceRZreportBIUnSendApproveBP {

	public AggRZreportBIVO[] unSend(AggRZreportBIVO[] clientBills,
			AggRZreportBIVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggRZreportBIVO> update = new BillUpdate<AggRZreportBIVO>();
		AggRZreportBIVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggRZreportBIVO[] clientBills) {
		for (AggRZreportBIVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
