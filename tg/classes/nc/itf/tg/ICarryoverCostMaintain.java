package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.pub.BusinessException;

public interface ICarryoverCostMaintain {

	public void delete(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] insert(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] update(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggCarrycost[] save(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] unsave(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] approve(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;

	public AggCarrycost[] unapprove(AggCarrycost[] clientFullVOs,
			AggCarrycost[] originBills) throws BusinessException;
}
