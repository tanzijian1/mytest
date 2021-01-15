package nc.impl.tg;

import nc.impl.pub.ace.AceMortgagelistdetailedPubServiceImpl;
import nc.itf.tg.IMortgagelistdetailed;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

public class MortgagelistdetailedImpl extends
		AceMortgagelistdetailedPubServiceImpl implements IMortgagelistdetailed {

	@Override
	public void delete(AggMortgageListDetailedVO[] vos)
			throws BusinessException {
		super.pubdeleteBills(vos);
	}

	@Override
	public AggMortgageListDetailedVO[] insert(AggMortgageListDetailedVO[] vos)
			throws BusinessException {
		return super.pubinsertBills(vos);
	}

	@Override
	public AggMortgageListDetailedVO[] update(AggMortgageListDetailedVO[] vos)
			throws BusinessException {
		return super.pubupdateBills(vos);
	}

	@Override
	public AggMortgageListDetailedVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMortgageListDetailedVO[] syncProjectData()
			throws BusinessException {
		return super.syncProjectData();
	}

}
