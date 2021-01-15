package nc.bs.tg.singleissue.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceSingleissueUnSendApproveBP {

	public AggSingleIssueVO[] unSend(AggSingleIssueVO[] clientBills,
			AggSingleIssueVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggSingleIssueVO> update = new BillUpdate<AggSingleIssueVO>();
		AggSingleIssueVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggSingleIssueVO[] clientBills) {
		for (AggSingleIssueVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
