package nc.bs.tg.marginworkorder.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceMarginWorkorderSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggMarginHVO[] sendApprove(AggMarginHVO[] clientBills,
			AggMarginHVO[] originBills) {
		for (AggMarginHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggMarginHVO[] returnVos = new BillUpdate<AggMarginHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
