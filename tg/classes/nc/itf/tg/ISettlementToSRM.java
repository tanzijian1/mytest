package nc.itf.tg;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public interface ISettlementToSRM {
	//核销回写srm
	public String SendSettlementToSRM(CircularlyAccessibleValueObject bvo) throws BusinessException;
	//反核销回写srm
	public String SendCancelSettlementToSRM(CircularlyAccessibleValueObject bvo) throws BusinessException;
}
