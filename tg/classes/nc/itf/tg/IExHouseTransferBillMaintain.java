package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.pub.BusinessException;

public interface IExHouseTransferBillMaintain {

	public void delete(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] insert(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] update(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggExhousetransferbillHVO[] save(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] unsave(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] approve(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;

	public AggExhousetransferbillHVO[] unapprove(AggExhousetransferbillHVO[] clientFullVOs,
			AggExhousetransferbillHVO[] originBills) throws BusinessException;
}
