package nc.impl.tg;

import nc.impl.pub.ace.AceTGRZ_MortgageAgreementPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;
import nc.vo.pub.BusinessException;

public class TGRZ_MortgageAgreementMaintainImpl extends AceTGRZ_MortgageAgreementPubServiceImpl
		implements ITGRZ_MortgageAgreementMaintain {

	@Override
	public void delete(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] insert(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] update(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMortgageAgreementVO[] save(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] unsave(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] approve(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMortgageAgreementVO[] unapprove(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
