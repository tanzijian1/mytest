package nc.itf.tg.settlement;

import nc.vo.cmp.settlement.SettlementAggVO;
import nc.vo.pub.BusinessException;

public interface ISettleAfterService {
		public String settleAfterWriteBack(SettlementAggVO[] aggvo) throws BusinessException;
}
