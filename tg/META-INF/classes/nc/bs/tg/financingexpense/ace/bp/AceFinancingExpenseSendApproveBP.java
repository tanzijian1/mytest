package nc.bs.tg.financingexpense.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

/**
 * ��׼���������BP
 */
public class AceFinancingExpenseSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggFinancexpenseVO[] sendApprove(AggFinancexpenseVO[] clientBills,
			AggFinancexpenseVO[] originBills) {
		for (AggFinancexpenseVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggFinancexpenseVO[] returnVos = new BillUpdate<AggFinancexpenseVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
