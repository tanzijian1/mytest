package nc.bs.tg.financingplan.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceFinancingPlanSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggFinancingPlan[] sendApprove(AggFinancingPlan[] clientBills,
			AggFinancingPlan[] originBills) {
		for (AggFinancingPlan clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggFinancingPlan[] returnVos = new BillUpdate<AggFinancingPlan>().update(
				clientBills, originBills);
		return returnVos;
	}
}
