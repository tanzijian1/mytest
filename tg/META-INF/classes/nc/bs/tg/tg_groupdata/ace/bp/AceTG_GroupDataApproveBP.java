package nc.bs.tg.tg_groupdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;

/**
 * 标准单据审核的BP
 */
public class AceTG_GroupDataApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggGroupDataVO[] approve(AggGroupDataVO[] clientBills,
			AggGroupDataVO[] originBills) {
		for (AggGroupDataVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggGroupDataVO> update = new BillUpdate<AggGroupDataVO>();
		AggGroupDataVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
