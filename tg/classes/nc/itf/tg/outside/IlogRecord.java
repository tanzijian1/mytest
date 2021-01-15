package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;

public interface IlogRecord {
	/**
	 * NC成本请款业务对应的付款单完付回写EBS 日志记录接口 2019-12-04-谈子健
	 */
	public void logRecord(OutsideLogVO logVO) throws BusinessException;
}
