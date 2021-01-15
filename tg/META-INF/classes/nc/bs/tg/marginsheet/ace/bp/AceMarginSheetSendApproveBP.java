package nc.bs.tg.marginsheet.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.marginsheet.AggMarginHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceMarginSheetSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggMarginHVO[] sendApprove(AggMarginHVO[] clientBills,
			AggMarginHVO[] originBills) {
		for (AggMarginHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggMarginHVO[] returnVos = new BillUpdate<AggMarginHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
