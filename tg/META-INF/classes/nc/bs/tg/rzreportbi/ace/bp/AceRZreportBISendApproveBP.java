package nc.bs.tg.rzreportbi.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceRZreportBISendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggRZreportBIVO[] sendApprove(AggRZreportBIVO[] clientBills,
			AggRZreportBIVO[] originBills) {
		for (AggRZreportBIVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggRZreportBIVO[] returnVos = new BillUpdate<AggRZreportBIVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
