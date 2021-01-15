package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.pub.BusinessException;

public interface IInvoicebillMaintain {

	public void delete(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] insert(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] update(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggInvoiceBillVO[] save(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] unsave(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] approve(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;

	public AggInvoiceBillVO[] unapprove(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException;
}
