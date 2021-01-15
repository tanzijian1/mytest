package nc.impl.tg;

import nc.impl.pub.ace.AceCapitalmarketrepayPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.vo.pub.BusinessException;

public class CapitalmarketrepayMaintainImpl extends AceCapitalmarketrepayPubServiceImpl
		implements ICapitalmarketrepayMaintain {

	@Override
	public void delete(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] insert(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] update(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMarketRepalayVO[] save(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] unsave(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] approve(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMarketRepalayVO[] unapprove(AggMarketRepalayVO[] clientFullVOs,
			AggMarketRepalayVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
