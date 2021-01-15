package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.vo.pub.BusinessException;

public interface ISealflowMaintain {

	public void delete(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] insert(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] update(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSealFlowVO[] save(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] unsave(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] approve(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;

	public AggSealFlowVO[] unapprove(AggSealFlowVO[] clientFullVOs,
			AggSealFlowVO[] originBills) throws BusinessException;
}
