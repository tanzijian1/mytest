package nc.bs.tg.organization.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.tg.organization.AggOrganizationVO;

/**
 * 标准单据送审的BP
 */
public class AceOrganizationSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggOrganizationVO[] sendApprove(AggOrganizationVO[] clientBills,
			AggOrganizationVO[] originBills) {
		for (AggOrganizationVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggOrganizationVO[] returnVos = new BillUpdate<AggOrganizationVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
