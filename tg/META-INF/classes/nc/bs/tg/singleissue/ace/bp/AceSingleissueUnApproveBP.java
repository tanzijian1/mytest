package nc.bs.tg.singleissue.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceSingleissueUnApproveBP {

	public AggSingleIssueVO[] unApprove(AggSingleIssueVO[] clientBills,
			AggSingleIssueVO[] originBills) {
		for (AggSingleIssueVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSingleIssueVO> update = new BillUpdate<AggSingleIssueVO>();
		AggSingleIssueVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
