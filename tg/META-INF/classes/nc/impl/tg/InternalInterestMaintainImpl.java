package nc.impl.tg;

import nc.impl.pub.ace.AceInternalInterestPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.itf.tg.IInternalInterestMaintain;
import nc.vo.pub.BusinessException;

public class InternalInterestMaintainImpl extends AceInternalInterestPubServiceImpl
		implements IInternalInterestMaintain {

	@Override
	public void delete(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] insert(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] update(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggInternalinterest[] save(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] unsave(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] approve(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInternalinterest[] unapprove(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
