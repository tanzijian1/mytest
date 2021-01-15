package nc.bs.tg.masterdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceMasterdataUnSendApproveBP {

	public AggMasterDataVO[] unSend(AggMasterDataVO[] clientBills,
			AggMasterDataVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggMasterDataVO> update = new BillUpdate<AggMasterDataVO>();
		AggMasterDataVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggMasterDataVO[] clientBills) {
		for (AggMasterDataVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
