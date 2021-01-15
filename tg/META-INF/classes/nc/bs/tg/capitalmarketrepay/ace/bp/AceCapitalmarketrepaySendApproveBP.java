package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceCapitalmarketrepaySendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggMarketRepalayVO[] sendApprove(AggMarketRepalayVO[] clientBills,
			AggMarketRepalayVO[] originBills) {
		for (AggMarketRepalayVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggMarketRepalayVO[] returnVos = new BillUpdate<AggMarketRepalayVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
