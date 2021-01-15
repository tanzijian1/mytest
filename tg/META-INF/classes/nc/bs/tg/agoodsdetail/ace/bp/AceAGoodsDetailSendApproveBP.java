package nc.bs.tg.agoodsdetail.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceAGoodsDetailSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggAGoodsDetail[] sendApprove(AggAGoodsDetail[] clientBills,
			AggAGoodsDetail[] originBills) {
		for (AggAGoodsDetail clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggAGoodsDetail[] returnVos = new BillUpdate<AggAGoodsDetail>().update(
				clientBills, originBills);
		return returnVos;
	}
}
