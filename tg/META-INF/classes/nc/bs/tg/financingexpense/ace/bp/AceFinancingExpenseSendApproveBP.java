package nc.bs.tg.financingexpense.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

/**
 * 标准单据送审的BP
 */
public class AceFinancingExpenseSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggFinancexpenseVO[] sendApprove(AggFinancexpenseVO[] clientBills,
			AggFinancexpenseVO[] originBills) {
		for (AggFinancexpenseVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggFinancexpenseVO[] returnVos = new BillUpdate<AggFinancexpenseVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
