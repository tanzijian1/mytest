package nc.bs.tg.taxcalculation.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * ��׼���������BP
 */
public class AceTaxCalculationSendApproveBP {
	/**
	 * ������
	 * 
	 * @param vos
	 *            ����VO����
	 * @param script
	 *            ���ݶ����ű�����
	 * @return �����ĵ���VO����
	 */

	public AggTaxCalculationHead[] sendApprove(AggTaxCalculationHead[] clientBills,
			AggTaxCalculationHead[] originBills) {
		for (AggTaxCalculationHead clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// ���ݳ־û�
		AggTaxCalculationHead[] returnVos = new BillUpdate<AggTaxCalculationHead>().update(
				clientBills, originBills);
		return returnVos;
	}
}
