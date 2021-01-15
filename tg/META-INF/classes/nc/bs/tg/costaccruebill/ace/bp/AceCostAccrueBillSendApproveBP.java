package nc.bs.tg.costaccruebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceCostAccrueBillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggCostAccrueBill[] sendApprove(AggCostAccrueBill[] clientBills,
			AggCostAccrueBill[] originBills) {
		for (AggCostAccrueBill clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggCostAccrueBill[] returnVos = new BillUpdate<AggCostAccrueBill>().update(
				clientBills, originBills);
		return returnVos;
	}
}
