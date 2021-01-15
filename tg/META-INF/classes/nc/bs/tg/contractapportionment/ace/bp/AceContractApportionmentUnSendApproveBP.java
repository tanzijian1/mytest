package nc.bs.tg.contractapportionment.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceContractApportionmentUnSendApproveBP {

	public AggContractAptmentVO[] unSend(AggContractAptmentVO[] clientBills,
			AggContractAptmentVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggContractAptmentVO> update = new BillUpdate<AggContractAptmentVO>();
		AggContractAptmentVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggContractAptmentVO[] clientBills) {
		for (AggContractAptmentVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
