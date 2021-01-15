package nc.impl.tg;

import nc.impl.pub.ace.AceApprovalproPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.itf.tg.IApprovalproMaintain;
import nc.vo.pub.BusinessException;

public class ApprovalproMaintainImpl extends AceApprovalproPubServiceImpl
		implements IApprovalproMaintain {

	@Override
	public void delete(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] insert(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] update(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggApprovalProVO[] save(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] unsave(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] approve(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggApprovalProVO[] unapprove(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
