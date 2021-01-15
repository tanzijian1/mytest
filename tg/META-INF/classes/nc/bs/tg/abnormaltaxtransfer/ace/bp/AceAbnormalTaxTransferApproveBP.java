package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

/**
 * 标准单据审核的BP
 */
public class AceAbnormalTaxTransferApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggAbTaxTransferHVO[] approve(AggAbTaxTransferHVO[] clientBills,
			AggAbTaxTransferHVO[] originBills) {
		for (AggAbTaxTransferHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
			clientBill.getParentVO().setBillstatus(1);
		}
		BillUpdate<AggAbTaxTransferHVO> update = new BillUpdate<AggAbTaxTransferHVO>();
		AggAbTaxTransferHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
