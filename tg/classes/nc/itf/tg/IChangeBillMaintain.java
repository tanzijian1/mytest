package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.pub.BusinessException;

public interface IChangeBillMaintain {

	public void delete(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] insert(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] update(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggChangeBillHVO[] save(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] unsave(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] approve(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;

	public AggChangeBillHVO[] unapprove(AggChangeBillHVO[] clientFullVOs,
			AggChangeBillHVO[] originBills) throws BusinessException;
}
