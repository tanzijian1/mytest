package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.standard.AggStandardVO;

public interface IStandardMaintain {

	public void delete(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException;

	public AggStandardVO[] insert(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException;

	public AggStandardVO[] update(AggStandardVO[] clientFullVOs,
			AggStandardVO[] originBills) throws BusinessException;

	public AggStandardVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

}
