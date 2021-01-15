package nc.bs.tg.contractapportionment.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;

/**
 * 标准单据审核的BP
 */
public class AceContractApportionmentApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggContractAptmentVO[] approve(AggContractAptmentVO[] clientBills,
			AggContractAptmentVO[] originBills) {
		for (AggContractAptmentVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggContractAptmentVO> update = new BillUpdate<AggContractAptmentVO>();
		AggContractAptmentVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
