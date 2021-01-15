package nc.bs.tg.organization.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.tg.organization.AggOrganizationVO;

/**
 * ��׼�����ջص�BP
 */
public class AceOrganizationUnSendApproveBP {

	public AggOrganizationVO[] unSend(AggOrganizationVO[] clientBills,
			AggOrganizationVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggOrganizationVO> update = new BillUpdate<AggOrganizationVO>();
		AggOrganizationVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggOrganizationVO[] clientBills) {
		for (AggOrganizationVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
