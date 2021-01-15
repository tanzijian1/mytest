package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.vo.pub.BusinessException;

public interface IInterestShareMaintain {

	public void delete(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] insert(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] update(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggIntshareHead[] save(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] unsave(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] approve(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;

	public AggIntshareHead[] unapprove(AggIntshareHead[] clientFullVOs,
			AggIntshareHead[] originBills) throws BusinessException;
}
