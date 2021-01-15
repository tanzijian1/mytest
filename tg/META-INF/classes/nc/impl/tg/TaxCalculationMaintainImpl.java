package nc.impl.tg;

import nc.impl.pub.ace.AceTaxCalculationPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;
import nc.vo.pub.BusinessException;

public class TaxCalculationMaintainImpl extends AceTaxCalculationPubServiceImpl
		implements ITaxCalculationMaintain {

	@Override
	public void delete(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] insert(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] update(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggTaxCalculationHead[] save(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] unsave(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] approve(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTaxCalculationHead[] unapprove(AggTaxCalculationHead[] clientFullVOs,
			AggTaxCalculationHead[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
