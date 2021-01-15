package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceCapitalmarketrepayUnSendApproveBP {

	public AggMarketRepalayVO[] unSend(AggMarketRepalayVO[] clientBills,
			AggMarketRepalayVO[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggMarketRepalayVO> update = new BillUpdate<AggMarketRepalayVO>();
		AggMarketRepalayVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggMarketRepalayVO[] clientBills) {
		for (AggMarketRepalayVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
