package nc.bs.tg.carryovercost.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceCarryoverCostSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggCarrycost[] sendApprove(AggCarrycost[] clientBills,
			AggCarrycost[] originBills) {
		for (AggCarrycost clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggCarrycost[] returnVos = new BillUpdate<AggCarrycost>().update(
				clientBills, originBills);
		return returnVos;
	}
}
