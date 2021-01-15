package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceAbnormalTaxTransferSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggAbTaxTransferHVO[] sendApprove(AggAbTaxTransferHVO[] clientBills,
			AggAbTaxTransferHVO[] originBills) {
		for (AggAbTaxTransferHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
			clientFullVO.getParentVO().setBillstatus(3);
		}
		// 数据持久化
		AggAbTaxTransferHVO[] returnVos = new BillUpdate<AggAbTaxTransferHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
