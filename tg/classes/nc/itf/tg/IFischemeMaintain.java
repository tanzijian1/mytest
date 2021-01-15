package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.pub.BusinessException;

public interface IFischemeMaintain {

	public void delete(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] insert(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] update(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggFIScemeHVO[] save(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] unsave(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] approve(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;

	public AggFIScemeHVO[] unapprove(AggFIScemeHVO[] clientFullVOs,
			AggFIScemeHVO[] originBills) throws BusinessException;
}
