package nc.bs.tg.contractapportionment.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceContractApportionmentSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggContractAptmentVO[] sendApprove(AggContractAptmentVO[] clientBills,
			AggContractAptmentVO[] originBills) {
		for (AggContractAptmentVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggContractAptmentVO[] returnVos = new BillUpdate<AggContractAptmentVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
