package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.pub.BusinessException;

public interface ICostAccrueBillMaintain {

	public void delete(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] insert(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] update(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggCostAccrueBill[] save(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] unsave(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] approve(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;

	public AggCostAccrueBill[] unapprove(AggCostAccrueBill[] clientFullVOs,
			AggCostAccrueBill[] originBills) throws BusinessException;
}
