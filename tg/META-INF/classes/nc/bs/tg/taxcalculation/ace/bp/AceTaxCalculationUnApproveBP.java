package nc.bs.tg.taxcalculation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceTaxCalculationUnApproveBP {

	public AggTaxCalculationHead[] unApprove(AggTaxCalculationHead[] clientBills,
			AggTaxCalculationHead[] originBills) {
		for (AggTaxCalculationHead clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggTaxCalculationHead> update = new BillUpdate<AggTaxCalculationHead>();
		AggTaxCalculationHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
