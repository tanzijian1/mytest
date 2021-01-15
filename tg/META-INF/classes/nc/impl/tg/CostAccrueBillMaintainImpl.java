package nc.impl.tg;

import nc.impl.pub.ace.AceCostAccrueBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.itf.tg.ICostAccrueBillMaintain;
import nc.vo.pub.BusinessException;

public class CostAccrueBillMaintainImpl extends AceCostAccrueBillPubServiceImpl
		implements ICostAccrueBillMaintain {

	@Override
	public void delete(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] insert(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] update(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggCostAccrueBill[] save(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] unsave(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] approve(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggCostAccrueBill[] unapprove(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
