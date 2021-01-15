package nc.impl.tg;

import nc.impl.pub.ace.AceAddticketPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.addticket.AggAddTicket;
import nc.itf.tg.IAddticketMaintain;
import nc.vo.pub.BusinessException;

public class AddticketMaintainImpl extends AceAddticketPubServiceImpl
		implements IAddticketMaintain {

	@Override
	public void delete(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] insert(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] update(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggAddTicket[] save(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] unsave(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] approve(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggAddTicket[] unapprove(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
