package nc.impl.tg;

import nc.impl.pub.ace.AceSalaryfundaccurePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.itf.tg.ISalaryfundaccureMaintain;
import nc.vo.pub.BusinessException;

public class SalaryfundaccureMaintainImpl extends AceSalaryfundaccurePubServiceImpl
		implements ISalaryfundaccureMaintain {

	@Override
	public void delete(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] insert(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] update(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggSalaryfundaccure[] save(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] unsave(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] approve(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalaryfundaccure[] unapprove(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
