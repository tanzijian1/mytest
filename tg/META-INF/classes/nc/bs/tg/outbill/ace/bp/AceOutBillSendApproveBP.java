package nc.bs.tg.outbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceOutBillSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggOutbillHVO[] sendApprove(AggOutbillHVO[] clientBills,
			AggOutbillHVO[] originBills) {
		for (AggOutbillHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// 数据持久化
		AggOutbillHVO[] returnVos = new BillUpdate<AggOutbillHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
