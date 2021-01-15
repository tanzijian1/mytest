package nc.impl.tg;

import nc.impl.pub.ace.AceMasterdataPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.itf.tg.IMasterdataMaintain;
import nc.vo.pub.BusinessException;

public class MasterdataMaintainImpl extends AceMasterdataPubServiceImpl
		implements IMasterdataMaintain {

	@Override
	public void delete(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] insert(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] update(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMasterDataVO[] save(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] unsave(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] approve(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMasterDataVO[] unapprove(AggMasterDataVO[] clientFullVOs,
			AggMasterDataVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
