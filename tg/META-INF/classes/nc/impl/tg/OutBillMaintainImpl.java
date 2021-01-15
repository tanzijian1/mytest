package nc.impl.tg;

import nc.impl.pub.ace.AceOutBillPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.itf.tg.IOutBillMaintain;
import nc.vo.pub.BusinessException;

public class OutBillMaintainImpl extends AceOutBillPubServiceImpl
		implements IOutBillMaintain {

	@Override
	public void delete(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] insert(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] update(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggOutbillHVO[] save(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] unsave(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] approve(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOutbillHVO[] unapprove(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
