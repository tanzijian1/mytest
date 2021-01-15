package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.pub.BusinessException;

public interface IRenameChangeBillMaintain {

	public void delete(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] insert(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] update(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggRenameChangeBillHVO[] save(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] unsave(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] approve(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;

	public AggRenameChangeBillHVO[] unapprove(AggRenameChangeBillHVO[] clientFullVOs,
			AggRenameChangeBillHVO[] originBills) throws BusinessException;
}
