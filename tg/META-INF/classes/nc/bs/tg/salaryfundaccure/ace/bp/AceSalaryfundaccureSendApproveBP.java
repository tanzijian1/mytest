package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceSalaryfundaccureSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggSalaryfundaccure[] sendApprove(AggSalaryfundaccure[] clientBills,
			AggSalaryfundaccure[] originBills) {
		for (AggSalaryfundaccure clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggSalaryfundaccure[] returnVos = new BillUpdate<AggSalaryfundaccure>().update(
				clientBills, originBills);
		return returnVos;
	}
}
