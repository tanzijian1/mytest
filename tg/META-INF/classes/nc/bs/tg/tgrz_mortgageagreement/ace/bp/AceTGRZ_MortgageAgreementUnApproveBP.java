package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceTGRZ_MortgageAgreementUnApproveBP {

	public AggMortgageAgreementVO[] unApprove(AggMortgageAgreementVO[] clientBills,
			AggMortgageAgreementVO[] originBills) {
		for (AggMortgageAgreementVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMortgageAgreementVO> update = new BillUpdate<AggMortgageAgreementVO>();
		AggMortgageAgreementVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
