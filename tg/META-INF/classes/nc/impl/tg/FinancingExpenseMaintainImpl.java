package nc.impl.tg;

import nc.impl.pub.ace.AceFinancingExpensePubServiceImpl;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class FinancingExpenseMaintainImpl extends AceFinancingExpensePubServiceImpl
		implements IFinancingExpenseMaintain {

	@Override
	public void delete(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] insert(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] update(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggFinancexpenseVO[] save(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] unsave(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] approve(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancexpenseVO[] unapprove(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
