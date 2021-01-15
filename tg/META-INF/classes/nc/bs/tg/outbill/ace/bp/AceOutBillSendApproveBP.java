package nc.bs.tg.outbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceOutBillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggOutbillHVO[] sendApprove(AggOutbillHVO[] clientBills,
			AggOutbillHVO[] originBills) {
		for (AggOutbillHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggOutbillHVO[] returnVos = new BillUpdate<AggOutbillHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
