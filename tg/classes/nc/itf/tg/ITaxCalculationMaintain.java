package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.pub.BusinessException;

public interface ITaxCalculationMaintain {

	public void delete(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] insert(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] update(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggTaxCalculationHead[] save(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] unsave(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] approve(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;

	public AggTaxCalculationHead[] unapprove(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException;
}
