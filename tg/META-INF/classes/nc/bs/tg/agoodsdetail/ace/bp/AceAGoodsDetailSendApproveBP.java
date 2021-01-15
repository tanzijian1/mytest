package nc.bs.tg.agoodsdetail.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceAGoodsDetailSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggAGoodsDetail[] sendApprove(AggAGoodsDetail[] clientBills,
			AggAGoodsDetail[] originBills) {
		for (AggAGoodsDetail clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggAGoodsDetail[] returnVos = new BillUpdate<AggAGoodsDetail>().update(
				clientBills, originBills);
		return returnVos;
	}
}
