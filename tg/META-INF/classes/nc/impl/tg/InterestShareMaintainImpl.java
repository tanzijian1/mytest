package nc.impl.tg;

import nc.impl.pub.ace.AceInterestSharePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;
import nc.vo.pub.BusinessException;

public class InterestShareMaintainImpl extends AceInterestSharePubServiceImpl
		implements IInterestShareMaintain {

	@Override
	public void delete(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] insert(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] update(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggIntshareHead[] save(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] unsave(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] approve(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggIntshareHead[] unapprove(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
