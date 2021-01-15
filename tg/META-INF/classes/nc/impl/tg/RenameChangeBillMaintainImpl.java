package nc.impl.tg;

import nc.impl.pub.ace.AceRenameChangeBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.itf.tg.IRenameChangeBillMaintain;
import nc.vo.pub.BusinessException;

public class RenameChangeBillMaintainImpl extends AceRenameChangeBillPubServiceImpl
		implements IRenameChangeBillMaintain {

	@Override
	public void delete(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] insert(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] update(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggRenameChangeBillHVO[] save(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] unsave(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] approve(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggRenameChangeBillHVO[] unapprove(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
