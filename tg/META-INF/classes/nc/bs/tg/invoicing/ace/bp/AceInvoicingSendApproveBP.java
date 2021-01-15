package nc.bs.tg.invoicing.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceInvoicingSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggInvoicingHead[] sendApprove(AggInvoicingHead[] clientBills,
			AggInvoicingHead[] originBills) {
		for (AggInvoicingHead clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggInvoicingHead[] returnVos = new BillUpdate<AggInvoicingHead>().update(
				clientBills, originBills);
		return returnVos;
	}
}
