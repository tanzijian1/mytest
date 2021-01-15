package nc.bs.tg.internalinterest.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceInternalInterestSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggInternalinterest[] sendApprove(AggInternalinterest[] clientBills,
			AggInternalinterest[] originBills) {
		for (AggInternalinterest clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// 数据持久化
		AggInternalinterest[] returnVos = new BillUpdate<AggInternalinterest>().update(
				clientBills, originBills);
		return returnVos;
	}
}
