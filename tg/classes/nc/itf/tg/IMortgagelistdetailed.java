package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

public interface IMortgagelistdetailed {
	public void delete(AggMortgageListDetailedVO[] vos)
			throws BusinessException;

	public AggMortgageListDetailedVO[] insert(AggMortgageListDetailedVO[] vos)
			throws BusinessException;

	public AggMortgageListDetailedVO[] update(AggMortgageListDetailedVO[] vos)
			throws BusinessException;

	public AggMortgageListDetailedVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMortgageListDetailedVO[] syncProjectData()
			throws BusinessException;
}
