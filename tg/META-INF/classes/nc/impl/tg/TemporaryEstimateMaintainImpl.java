package nc.impl.tg;

import nc.impl.pub.ace.AceTemporaryEstimatePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.itf.tg.ITemporaryEstimateMaintain;
import nc.vo.pub.BusinessException;

public class TemporaryEstimateMaintainImpl extends AceTemporaryEstimatePubServiceImpl
		implements ITemporaryEstimateMaintain {

	@Override
	public void delete(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] insert(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] update(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggTemest[] save(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] unsave(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] approve(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTemest[] unapprove(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
