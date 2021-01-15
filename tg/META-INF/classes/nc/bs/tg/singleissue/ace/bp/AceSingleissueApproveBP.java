package nc.bs.tg.singleissue.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.singleissue.AggSingleIssueVO;

/**
 * ��׼������˵�BP
 */
public class AceSingleissueApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggSingleIssueVO[] approve(AggSingleIssueVO[] clientBills,
			AggSingleIssueVO[] originBills) {
		for (AggSingleIssueVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSingleIssueVO> update = new BillUpdate<AggSingleIssueVO>();
		AggSingleIssueVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
