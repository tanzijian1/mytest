package nc.bs.tg.singleissue.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceSingleissueSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggSingleIssueVO[] sendApprove(AggSingleIssueVO[] clientBills,
			AggSingleIssueVO[] originBills) {
		for (AggSingleIssueVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggSingleIssueVO[] returnVos = new BillUpdate<AggSingleIssueVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
