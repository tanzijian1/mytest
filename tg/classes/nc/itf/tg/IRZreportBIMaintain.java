package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.vo.pub.BusinessException;

public interface IRZreportBIMaintain {

	public void delete(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] insert(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] update(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggRZreportBIVO[] save(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] unsave(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] approve(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;

	public AggRZreportBIVO[] unapprove(AggRZreportBIVO[] clientFullVOs,
			AggRZreportBIVO[] originBills) throws BusinessException;
}
