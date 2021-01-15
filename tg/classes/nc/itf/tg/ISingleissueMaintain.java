package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.pub.BusinessException;

public interface ISingleissueMaintain {

	public void delete(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] insert(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] update(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSingleIssueVO[] save(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] unsave(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] approve(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;

	public AggSingleIssueVO[] unapprove(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) throws BusinessException;
}
