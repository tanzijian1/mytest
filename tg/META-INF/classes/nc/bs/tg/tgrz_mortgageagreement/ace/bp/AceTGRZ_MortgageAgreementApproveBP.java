package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

/**
 * 标准单据审核的BP
 */
public class AceTGRZ_MortgageAgreementApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggMortgageAgreementVO[] approve(AggMortgageAgreementVO[] clientBills,
			AggMortgageAgreementVO[] originBills) {
		for (AggMortgageAgreementVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMortgageAgreementVO> update = new BillUpdate<AggMortgageAgreementVO>();
		AggMortgageAgreementVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
