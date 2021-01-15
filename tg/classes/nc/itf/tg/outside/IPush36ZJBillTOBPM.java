package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.sf.allocateapply.AggAllocateApplyVO;
import nc.vo.sf.fundtransferapply.AggFundTransferApplyVO;

public interface IPush36ZJBillTOBPM {
		public String onTrans36K1DataTOBPM(AggAllocateApplyVO applyvo) throws BusinessException;//下拨申请
		public String onTrans36K5DataTOBPM(AggFundTransferApplyVO trsnapplyvo) throws BusinessException;//资金调拨

}
