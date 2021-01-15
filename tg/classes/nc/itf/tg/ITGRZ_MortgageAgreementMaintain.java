package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.pub.BusinessException;

public interface ITGRZ_MortgageAgreementMaintain {

	public void delete(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] insert(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] update(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMortgageAgreementVO[] save(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] unsave(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] approve(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;

	public AggMortgageAgreementVO[] unapprove(AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) throws BusinessException;
}
