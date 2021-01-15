package nc.bs.tg.temporaryestimate.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceTemporaryEstimateSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggTemest[] sendApprove(AggTemest[] clientBills,
			AggTemest[] originBills) {
		for (AggTemest clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// 数据持久化
		AggTemest[] returnVos = new BillUpdate<AggTemest>().update(
				clientBills, originBills);
		return returnVos;
	}
}
