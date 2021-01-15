package nc.impl.tg;

import nc.impl.pub.ace.AceStandardPubServiceImpl;
import nc.itf.tg.IStandardMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.standard.AggStandardVO;

public class StandardMaintainImpl extends AceStandardPubServiceImpl implements
		IStandardMaintain {

	@Override
	public void delete(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggStandardVO[] insert(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggStandardVO[] update(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggStandardVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

}
