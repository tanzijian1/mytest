package nc.bs.tg.masterdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.tg.masterdata.AggMasterDataVO;

/**
 * 标准单据审核的BP
 */
public class AceMasterdataApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggMasterDataVO[] approve(AggMasterDataVO[] clientBills,
			AggMasterDataVO[] originBills) {
		for (AggMasterDataVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMasterDataVO> update = new BillUpdate<AggMasterDataVO>();
		AggMasterDataVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}
