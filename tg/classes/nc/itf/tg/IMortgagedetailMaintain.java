package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.mortgagedetail.AggMortgageDetalVO;
import nc.vo.pub.BusinessException;

public interface IMortgagedetailMaintain {

	public void delete(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] insert(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] update(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMortgageDetalVO[] save(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] unsave(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] approve(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;

	public AggMortgageDetalVO[] unapprove(AggMortgageDetalVO[] clientFullVOs,
			AggMortgageDetalVO[] originBills) throws BusinessException;
}
