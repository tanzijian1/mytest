package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.pub.BusinessException;

public interface IAddticketMaintain {

	public void delete(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] insert(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] update(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggAddTicket[] save(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] unsave(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] approve(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;

	public AggAddTicket[] unapprove(AggAddTicket[] clientFullVOs,
			AggAddTicket[] originBills) throws BusinessException;
}
