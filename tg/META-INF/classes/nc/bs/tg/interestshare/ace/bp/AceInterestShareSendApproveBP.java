package nc.bs.tg.interestshare.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceInterestShareSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggIntshareHead[] sendApprove(AggIntshareHead[] clientBills,
			AggIntshareHead[] originBills) {
		for (AggIntshareHead clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggIntshareHead[] returnVos = new BillUpdate<AggIntshareHead>().update(
				clientBills, originBills);
		return returnVos;
	}
}
