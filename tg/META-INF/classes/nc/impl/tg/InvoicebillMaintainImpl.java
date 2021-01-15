package nc.impl.tg;

import nc.impl.pub.ace.AceInvoicebillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.itf.tg.IInvoicebillMaintain;
import nc.vo.pub.BusinessException;

public class InvoicebillMaintainImpl extends AceInvoicebillPubServiceImpl
		implements IInvoicebillMaintain {

	@Override
	public void delete(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] insert(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] update(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggInvoiceBillVO[] save(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] unsave(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] approve(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggInvoiceBillVO[] unapprove(AggInvoiceBillVO[] clientFullVOs,
			AggInvoiceBillVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
