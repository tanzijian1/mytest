package nc.bs.tg.checkfinance.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceCheckfinanceUnSendApproveBP {

	public AggCheckFinanceHVO[] unSend(AggCheckFinanceHVO[] clientBills,
			AggCheckFinanceHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggCheckFinanceHVO> update = new BillUpdate<AggCheckFinanceHVO>();
		AggCheckFinanceHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggCheckFinanceHVO[] clientBills) {
		for (AggCheckFinanceHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
