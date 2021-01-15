package nc.bs.tg.checkfinance.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceCheckfinanceSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggCheckFinanceHVO[] sendApprove(AggCheckFinanceHVO[] clientBills,
			AggCheckFinanceHVO[] originBills) {
		for (AggCheckFinanceHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggCheckFinanceHVO[] returnVos = new BillUpdate<AggCheckFinanceHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
