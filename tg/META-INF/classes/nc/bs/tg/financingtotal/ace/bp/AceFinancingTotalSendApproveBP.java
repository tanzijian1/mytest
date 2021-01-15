package nc.bs.tg.financingtotal.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceFinancingTotalSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggFinancingTotal[] sendApprove(AggFinancingTotal[] clientBills,
			AggFinancingTotal[] originBills) {
		for (AggFinancingTotal clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggFinancingTotal[] returnVos = new BillUpdate<AggFinancingTotal>().update(
				clientBills, originBills);
		return returnVos;
	}
}
