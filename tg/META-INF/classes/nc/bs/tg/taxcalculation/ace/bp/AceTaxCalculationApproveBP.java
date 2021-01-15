package nc.bs.tg.taxcalculation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;

/**
 * 标准单据审核的BP
 */
public class AceTaxCalculationApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggTaxCalculationHead[] approve(AggTaxCalculationHead[] clientBills,
			AggTaxCalculationHead[] originBills) {
		for (AggTaxCalculationHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggTaxCalculationHead> update = new BillUpdate<AggTaxCalculationHead>();
		AggTaxCalculationHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
