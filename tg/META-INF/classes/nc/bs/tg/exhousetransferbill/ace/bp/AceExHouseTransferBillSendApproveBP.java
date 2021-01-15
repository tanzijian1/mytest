package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceExHouseTransferBillSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggExhousetransferbillHVO[] sendApprove(AggExhousetransferbillHVO[] clientBills,
			AggExhousetransferbillHVO[] originBills) {
		for (AggExhousetransferbillHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// 数据持久化
		AggExhousetransferbillHVO[] returnVos = new BillUpdate<AggExhousetransferbillHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
