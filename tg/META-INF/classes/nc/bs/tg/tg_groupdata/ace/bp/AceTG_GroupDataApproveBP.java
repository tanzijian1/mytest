package nc.bs.tg.tg_groupdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;

/**
 * ��׼������˵�BP
 */
public class AceTG_GroupDataApproveBP {

	/**
	 * ��˶���
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
