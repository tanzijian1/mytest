package nc.bs.tg.transferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceTransferBillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggTransferBillHVO[] sendApprove(AggTransferBillHVO[] clientBills,
			AggTransferBillHVO[] originBills) {
		for (AggTransferBillHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggTransferBillHVO[] returnVos = new BillUpdate<AggTransferBillHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
