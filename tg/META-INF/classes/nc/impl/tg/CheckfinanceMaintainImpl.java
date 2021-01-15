package nc.impl.tg;

import nc.impl.pub.ace.AceCheckfinancePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.itf.tg.ICheckfinanceMaintain;
import nc.vo.pub.BusinessException;

public class CheckfinanceMaintainImpl extends AceCheckfinancePubServiceImpl
		implements ICheckfinanceMaintain {

	@Override
	public void delete(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] insert(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] update(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggCheckFinanceHVO[] save(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] unsave(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] approve(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCheckFinanceHVO[] unapprove(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
