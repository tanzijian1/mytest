package nc.impl.tg;

import nc.impl.pub.ace.AceCarryoverCostPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.itf.tg.ICarryoverCostMaintain;
import nc.vo.pub.BusinessException;

public class CarryoverCostMaintainImpl extends AceCarryoverCostPubServiceImpl
		implements ICarryoverCostMaintain {

	@Override
	public void delete(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] insert(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] update(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggCarrycost[] save(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] unsave(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] approve(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCarrycost[] unapprove(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
