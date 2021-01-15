package nc.bs.tg.tartingbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceTartingBillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggTartingBillVO[] sendApprove(AggTartingBillVO[] clientBills,
			AggTartingBillVO[] originBills) {
		for (AggTartingBillVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggTartingBillVO[] returnVos = new BillUpdate<AggTartingBillVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
