package nc.itf.tg;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

public interface ISettlementToSRM {
	//������дsrm
	public String SendSettlementToSRM(CircularlyAccessibleValueObject bvo) throws BusinessException;
	//��������дsrm
	public String SendCancelSettlementToSRM(CircularlyAccessibleValueObject bvo) throws BusinessException;
}
