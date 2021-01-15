package nc.impl.tg;

import nc.impl.pub.ace.AceFischemepushstandardPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;
import nc.vo.pub.BusinessException;

public class FischemepushstandardMaintainImpl extends AceFischemepushstandardPubServiceImpl
		implements IFischemepushstandardMaintain {

	@Override
	public void delete(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] insert(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] update(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggFischemePushStandardHVO[] save(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] unsave(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] approve(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFischemePushStandardHVO[] unapprove(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
