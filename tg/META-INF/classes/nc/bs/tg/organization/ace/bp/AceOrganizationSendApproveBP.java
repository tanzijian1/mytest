package nc.bs.tg.organization.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.tg.organization.AggOrganizationVO;

/**
 * ��׼���������BP
 */
public class AceOrganizationSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggOrganizationVO[] sendApprove(AggOrganizationVO[] clientBills,
			AggOrganizationVO[] originBills) {
		for (AggOrganizationVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggOrganizationVO[] returnVos = new BillUpdate<AggOrganizationVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
