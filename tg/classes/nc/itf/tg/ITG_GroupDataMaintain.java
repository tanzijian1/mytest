package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.vo.pub.BusinessException;

public interface ITG_GroupDataMaintain {

	public void delete(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] insert(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] update(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggGroupDataVO[] save(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] unsave(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] approve(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;

	public AggGroupDataVO[] unapprove(AggGroupDataVO[] clientFullVOs,
			AggGroupDataVO[] originBills) throws BusinessException;
}
