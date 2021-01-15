package nc.impl.tg;

import nc.impl.pub.ace.AceFinancingTotalPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.itf.tg.IFinancingTotalMaintain;
import nc.vo.pub.BusinessException;

public class FinancingTotalMaintainImpl extends AceFinancingTotalPubServiceImpl
		implements IFinancingTotalMaintain {

	@Override
	public void delete(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] insert(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] update(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggFinancingTotal[] save(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] unsave(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] approve(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingTotal[] unapprove(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
