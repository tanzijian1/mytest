package nc.bs.tg.taxcalculation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼�����ջص�BP
 */
public class AceTaxCalculationUnSendApproveBP {

	public AggTaxCalculationHead[] unSend(AggTaxCalculationHead[] clientBills,
			AggTaxCalculationHead[] originBills) {
		// ��VO�־û������ݿ���
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggTaxCalculationHead> update = new BillUpdate<AggTaxCalculationHead>();
		AggTaxCalculationHead[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggTaxCalculationHead[] clientBills) {
		for (AggTaxCalculationHead clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}
