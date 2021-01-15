package nc.bs.tg.outside.infotransform;

import java.util.HashMap;
import java.util.List;

import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

/**
 * ʱ���ڲ�����ӿ���
 * 
 * @author acer
 * 
 */
public interface IInfoTransformService {

	/**
	 * ���ͳɱ��������������Ϣ
	 * 
	 * @return
	 */
	public List<Object[]> pushEbsCostPayBillPaid(PayBillItemVO[] payBillItemVOs)
			throws BusinessException;

	/**
	 * ���ͳɱ��������뵥����������Ϣ
	 * 
	 * @return
	 */
	public String pushEbsCostPayRequestPaid(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * ����SRM�������뵥����������Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pushSrmPayRequestPaid(AggPayrequest aggvo) throws Exception;

	/*
	 * ����ͨ�ø�����뵥
	 */
	public String pushEbsgeneralPayrquest(AggPayrequest aggvo) throws Exception;

	/*
	 * ����ͨ�ø��
	 */
	public String pushEbsgeneralPay(AggPayBillVO aggvo) throws Exception;

	/**
	 * �������뵥�˻�(SRM)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public String pushSRMthPayrequest(AggPayrequest aggvo) throws Exception;

	/**
	 * �������뵥�˻�(ͨ��)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String pushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * Ӧ�����˻�(ͨ��)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String pushEBSthPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * �������뵥�˻�(�ɱ�) 2019-12-25-tzj
	 */
	public String costPushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * Ӧ�������˻�(�ɱ�) 2019-12-25-tzj
	 */
	public String costPushEBSPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * ��Ʊ��д 2019-12-25-tzj
	 */
	public String bondtoSRM(AggGatheringBillVO aggvo) throws BusinessException;

	/**
	 * Ӧ��������Ʊ��ebs(�ɱ�) 2020-04-03-̸�ӽ�
	 */
	public String CostInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException;

	/**
	 * Ӧ��������Ʊ��ebs(ͨ��) 2020-04-03-̸�ӽ�
	 */
	public String TranInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException;

	/**
	 * ��Ʊ������Ʊ huangxj
	 * 
	 * @throws Exception
	 */
	public String OpenInvoice(HashMap<String, Object> value) throws Exception;

	/**
	 * ռԤ��+��˰��Ӧ��������ͨ����ebs(�ɱ�) 2020-06-05-̸�ӽ�
	 */
	public String CostBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,String flag)
			throws BusinessException;

	/**
	 * ռԤ��+��˰��Ӧ��������ͨ����ebs(ͨ��) 2020-06-05-̸�ӽ�
	 */
	public String TranBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,String flag)
			throws BusinessException;
}
