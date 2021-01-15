package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.pub.BusinessException;

public interface IOutBillMaintain {

	public void delete(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] insert(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] update(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggOutbillHVO[] save(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] unsave(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] approve(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;

	public AggOutbillHVO[] unapprove(AggOutbillHVO[] clientFullVOs,
			AggOutbillHVO[] originBills) throws BusinessException;
}
