package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceTGRZ_MortgageAgreementUnSendApproveBP {

	public AggMortgageAgreementVO[] unSend(AggMortgageAgreementVO[] clientBills,
			AggMortgageAgreementVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggMortgageAgreementVO> update = new BillUpdate<AggMortgageAgreementVO>();
		AggMortgageAgreementVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggMortgageAgreementVO[] clientBills) {
		for (AggMortgageAgreementVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
