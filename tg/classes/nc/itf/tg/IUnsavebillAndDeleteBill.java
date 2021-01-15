package nc.itf.tg;

import java.util.HashMap;

import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

public interface IUnsavebillAndDeleteBill {
	/**
	 * ��������ʱ���ղ�ɾ������ 2020-03-25-̸�ӽ�
	 */
	public void UnsavebillAndDelete_RequiresNew(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * ��������ʱ���ղ�ɾ������ 2020-05-21-huangxj
	 * 
	 * @return
	 */
	public Boolean BackAndDeletePaybill_RequiresNew(Object obj, String msg)
			throws BusinessException;

	/**
	 * ɾ��������ebs�������뵥 2020-08-07-huangxj
	 * 
	 * @param obj
	 * @param msg
	 * @throws BusinessException
	 */
	public void BackAndDeletePayreq_RequiresNew(AggPayrequest aggvo,
			int approvestatus) throws BusinessException;

	/**
	 * ɾ����������ҵ�տ 2020-08-07-huangxj
	 * 
	 * @param obj
	 * @param msg
	 * @throws BusinessException
	 */
	public void BackAndDeleteGatherBill_RequiresNew(Object obj,String type) throws BusinessException;
	
	/**
	 * ����ϵͳ�˻�ɾ��
	 * @param obj
	 * @param billType
	 * @param map
	 * @throws BusinessException
	 */
	public void SaleBackDelete_RequiresNew(AggregatedValueObject obj,String billType,HashMap<String, Object> map) throws BusinessException;

}
