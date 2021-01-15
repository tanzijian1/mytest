package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.vo.pub.BusinessException;

public interface ISalaryfundaccureMaintain {

	public void delete(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] insert(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] update(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSalaryfundaccure[] save(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] unsave(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] approve(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;

	public AggSalaryfundaccure[] unapprove(AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) throws BusinessException;
}
