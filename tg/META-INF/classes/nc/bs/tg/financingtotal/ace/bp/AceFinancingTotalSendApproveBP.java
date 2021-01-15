package nc.bs.tg.financingtotal.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceFinancingTotalSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggFinancingTotal[] sendApprove(AggFinancingTotal[] clientBills,
			AggFinancingTotal[] originBills) {
		for (AggFinancingTotal clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggFinancingTotal[] returnVos = new BillUpdate<AggFinancingTotal>().update(
				clientBills, originBills);
		return returnVos;
	}
}
