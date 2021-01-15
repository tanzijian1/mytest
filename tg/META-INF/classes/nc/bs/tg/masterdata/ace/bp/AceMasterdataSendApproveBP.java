package nc.bs.tg.masterdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceMasterdataSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggMasterDataVO[] sendApprove(AggMasterDataVO[] clientBills,
			AggMasterDataVO[] originBills) {
		for (AggMasterDataVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggMasterDataVO[] returnVos = new BillUpdate<AggMasterDataVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}
