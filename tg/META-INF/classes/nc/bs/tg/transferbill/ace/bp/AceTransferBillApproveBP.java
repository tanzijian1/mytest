package nc.bs.tg.transferbill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;

/**
 * ��׼������˵�BP
 */
public class AceTransferBillApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggTransferBillHVO[] approve(AggTransferBillHVO[] clientBills,
			AggTransferBillHVO[] originBills) {
		for (AggTransferBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
			clientBill.getParentVO().setEffectdate(new UFDateTime());
		}
		BillUpdate<AggTransferBillHVO> update = new BillUpdate<AggTransferBillHVO>();
		AggTransferBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
