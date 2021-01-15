package nc.itf.tg;

import java.util.HashMap;

import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public interface IUnsavebillAndDeleteBill {
	/**
	 * 单据作废时回收并删除单据 2020-03-25-谈子健
	 */
	public void UnsavebillAndDelete_RequiresNew(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * 单据作废时回收并删除单据 2020-05-21-huangxj
	 * 
	 * @return
	 */
	public Boolean BackAndDeletePaybill_RequiresNew(Object obj, String msg)
			throws BusinessException;

	/**
	 * 删除并推送ebs付款申请单 2020-08-07-huangxj
	 * 
	 * @param obj
	 * @param msg
	 * @throws BusinessException
	 */
	public void BackAndDeletePayreq_RequiresNew(AggPayrequest aggvo,
			int approvestatus) throws BusinessException;

	/**
	 * 删除并推送商业收款单 2020-08-07-huangxj
	 * 
	 * @param obj
	 * @param msg
	 * @throws BusinessException
	 */
	public void BackAndDeleteGatherBill_RequiresNew(Object obj,String type) throws BusinessException;
	
	/**
	 * 销售系统退回删单
	 * @param obj
	 * @param billType
	 * @param map
	 * @throws BusinessException
	 */
	public void SaleBackDelete_RequiresNew(AggregatedValueObject obj,String billType,HashMap<String, Object> map) throws BusinessException;

}
