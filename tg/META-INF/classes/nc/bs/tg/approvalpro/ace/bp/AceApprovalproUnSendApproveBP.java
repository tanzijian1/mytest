package nc.bs.tg.approvalpro.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceApprovalproUnSendApproveBP {

	public AggApprovalProVO[] unSend(AggApprovalProVO[] clientBills,
			AggApprovalProVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggApprovalProVO> update = new BillUpdate<AggApprovalProVO>();
		AggApprovalProVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggApprovalProVO[] clientBills) {
		for (AggApprovalProVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
