package nc.impl.tg;

import nc.impl.pub.ace.AceTG_GroupDataPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.itf.tg.ITG_GroupDataMaintain;
import nc.vo.pub.BusinessException;

public class TG_GroupDataMaintainImpl extends AceTG_GroupDataPubServiceImpl
		implements ITG_GroupDataMaintain {

	@Override
	public void delete(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] insert(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] update(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggGroupDataVO[] save(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] unsave(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] approve(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGroupDataVO[] unapprove(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
