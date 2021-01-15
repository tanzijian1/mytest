package nc.bs.tg.invoicebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceInvoicebillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggInvoiceBillVO[] sendApprove(AggInvoiceBillVO[] clientBills,
			AggInvoiceBillVO[] originBills) {
		for (AggInvoiceBillVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggInvoiceBillVO[] returnVos = new BillUpdate<AggInvoiceBillVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
