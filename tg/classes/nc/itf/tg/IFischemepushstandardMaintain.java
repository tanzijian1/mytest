package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.vo.pub.BusinessException;

public interface IFischemepushstandardMaintain {

	public void delete(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] insert(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] update(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggFischemePushStandardHVO[] save(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] unsave(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] approve(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;

	public AggFischemePushStandardHVO[] unapprove(AggFischemePushStandardHVO[] clientFullVOs,
			AggFischemePushStandardHVO[] originBills) throws BusinessException;
}
