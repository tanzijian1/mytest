package nc.impl.tg;

import nc.impl.pub.ace.AceMarginWorkorderPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.itf.tg.IMarginWorkorderMaintain;
import nc.vo.pub.BusinessException;

public class MarginWorkorderMaintainImpl extends AceMarginWorkorderPubServiceImpl
		implements IMarginWorkorderMaintain {

	@Override
	public void delete(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] insert(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] update(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMarginHVO[] save(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] unsave(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] approve(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarginHVO[] unapprove(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
