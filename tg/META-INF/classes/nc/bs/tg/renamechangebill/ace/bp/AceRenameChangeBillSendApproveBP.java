package nc.bs.tg.renamechangebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceRenameChangeBillSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggRenameChangeBillHVO[] sendApprove(AggRenameChangeBillHVO[] clientBills,
			AggRenameChangeBillHVO[] originBills) {
		for (AggRenameChangeBillHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
			clientFullVO.getParentVO().setEffectstatus(5);
		}
		// ���ݳ־û�
		AggRenameChangeBillHVO[] returnVos = new BillUpdate<AggRenameChangeBillHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
