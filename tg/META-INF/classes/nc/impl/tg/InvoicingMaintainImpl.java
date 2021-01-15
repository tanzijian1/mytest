package nc.impl.tg;

import nc.impl.pub.ace.AceInvoicingPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.itf.tg.IInvoicingMaintain;
import nc.vo.pub.BusinessException;

public class InvoicingMaintainImpl extends AceInvoicingPubServiceImpl
		implements IInvoicingMaintain {

	@Override
	public void delete(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] insert(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] update(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggInvoicingHead[] save(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] unsave(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] approve(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoicingHead[] unapprove(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
