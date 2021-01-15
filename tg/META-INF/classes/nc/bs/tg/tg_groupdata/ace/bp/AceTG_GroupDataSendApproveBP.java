package nc.bs.tg.tg_groupdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceTG_GroupDataSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggGroupDataVO[] sendApprove(AggGroupDataVO[] clientBills,
			AggGroupDataVO[] originBills) {
		for (AggGroupDataVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggGroupDataVO[] returnVos = new BillUpdate<AggGroupDataVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
