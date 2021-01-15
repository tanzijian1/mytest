package nc.bs.tg.renamechangebill.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

/**
 * ��׼������˵�BP
 */
public class AceRenameChangeBillApproveBP {

	/**
	 * ��˶���
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggRenameChangeBillHVO[] approve(AggRenameChangeBillHVO[] clientBills,
			AggRenameChangeBillHVO[] originBills) {
		for (AggRenameChangeBillHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
			clientBill.getParentVO().setEffectstatus(10);
			clientBill.getParentVO().setEffectdate(new UFDateTime());
		}
		BillUpdate<AggRenameChangeBillHVO> update = new BillUpdate<AggRenameChangeBillHVO>();
		AggRenameChangeBillHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
