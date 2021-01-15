package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceSalaryfundaccureSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggSalaryfundaccure[] sendApprove(AggSalaryfundaccure[] clientBills,
			AggSalaryfundaccure[] originBills) {
		for (AggSalaryfundaccure clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggSalaryfundaccure[] returnVos = new BillUpdate<AggSalaryfundaccure>().update(
				clientBills, originBills);
		return returnVos;
	}
}
