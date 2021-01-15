package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.vo.pub.BusinessException;

public interface ITransferBillMaintain {

	public void delete(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] insert(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] update(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggTransferBillHVO[] save(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] unsave(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] approve(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;

	public AggTransferBillHVO[] unapprove(AggTransferBillHVO[] clientFullVOs,
			AggTransferBillHVO[] originBills) throws BusinessException;
}
