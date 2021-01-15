package nc.bs.tg.organization.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.organization.AggOrganizationVO;

/**
 * 标准单据弃审的BP
 */
public class AceOrganizationUnApproveBP {

	public AggOrganizationVO[] unApprove(AggOrganizationVO[] clientBills,
			AggOrganizationVO[] originBills) {
		for (AggOrganizationVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggOrganizationVO> update = new BillUpdate<AggOrganizationVO>();
		AggOrganizationVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
