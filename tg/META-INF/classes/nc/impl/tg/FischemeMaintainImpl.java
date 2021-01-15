package nc.impl.tg;

import nc.impl.pub.ace.AceFischemePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.itf.tg.IFischemeMaintain;
import nc.vo.pub.BusinessException;

public class FischemeMaintainImpl extends AceFischemePubServiceImpl
		implements IFischemeMaintain {

	@Override
	public void delete(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] insert(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] update(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggFIScemeHVO[] save(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] unsave(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] approve(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFIScemeHVO[] unapprove(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
