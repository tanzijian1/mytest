package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.pub.BusinessException;

public interface ITartingBillMaintain {

	public void delete(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] insert(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] update(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggTartingBillVO[] save(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] unsave(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] approve(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;

	public AggTartingBillVO[] unapprove(AggTartingBillVO[] clientFullVOs,
			AggTartingBillVO[] originBills) throws BusinessException;
}
