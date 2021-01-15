package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.pub.BusinessException;

public interface IMarginWorkorderMaintain {

	public void delete(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] insert(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] update(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMarginHVO[] save(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] unsave(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] approve(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;

	public AggMarginHVO[] unapprove(AggMarginHVO[] clientFullVOs,
			AggMarginHVO[] originBills) throws BusinessException;
}
