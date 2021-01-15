package nc.impl.tg;

import nc.impl.pub.ace.AceChangeBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.itf.tg.IChangeBillMaintain;
import nc.vo.pub.BusinessException;

public class ChangeBillMaintainImpl extends AceChangeBillPubServiceImpl
		implements IChangeBillMaintain {

	@Override
	public void delete(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] insert(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] update(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggChangeBillHVO[] save(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] unsave(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] approve(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggChangeBillHVO[] unapprove(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
