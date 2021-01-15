package nc.bs.tg.contractapportionment.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceContractApportionmentUnApproveBP {

	public AggContractAptmentVO[] unApprove(AggContractAptmentVO[] clientBills,
			AggContractAptmentVO[] originBills) {
		for (AggContractAptmentVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggContractAptmentVO> update = new BillUpdate<AggContractAptmentVO>();
		AggContractAptmentVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
