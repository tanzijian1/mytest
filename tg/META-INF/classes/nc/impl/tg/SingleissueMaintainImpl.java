package nc.impl.tg;

import nc.impl.pub.ace.AceSingleissuePubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.itf.tg.ISingleissueMaintain;
import nc.vo.pub.BusinessException;

public class SingleissueMaintainImpl extends AceSingleissuePubServiceImpl
		implements ISingleissueMaintain {

	@Override
	public void delete(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] insert(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] update(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggSingleIssueVO[] save(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] unsave(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] approve(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSingleIssueVO[] unapprove(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
