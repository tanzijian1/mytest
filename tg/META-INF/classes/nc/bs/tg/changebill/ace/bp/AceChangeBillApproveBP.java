package nc.bs.tg.changebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.changebill.AggChangeBillHVO;

/**
 * 标准单据审核的BP
 */
public class AceChangeBillApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggChangeBillHVO[] approve(AggChangeBillHVO[] clientBills,
			AggChangeBillHVO[] originBills) {
		for (AggChangeBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
			clientBill.getParentVO().setEffectdate(new UFDateTime());
		}
		BillUpdate<AggChangeBillHVO> update = new BillUpdate<AggChangeBillHVO>();
		AggChangeBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
