package nc.bs.tg.masterdata.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.tg.masterdata.AggMasterDataVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceMasterdataUnApproveBP {

	public AggMasterDataVO[] unApprove(AggMasterDataVO[] clientBills,
			AggMasterDataVO[] originBills) {
		for (AggMasterDataVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggMasterDataVO> update = new BillUpdate<AggMasterDataVO>();
		AggMasterDataVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}
