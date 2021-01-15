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
 * 时代内部改造接口类
 * 
 * @author acer
 * 
 */
public interface IInfoTransformService {

	/**
	 * 推送成本付款单付讫结算信息
	 * 
	 * @return
	 */
	public List<Object[]> pushEbsCostPayBillPaid(PayBillItemVO[] payBillItemVOs)
			throws BusinessException;

	/**
	 * 推送成本付款申请单付讫结算信息
	 * 
	 * @return
	 */
	public String pushEbsCostPayRequestPaid(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * 推送SRM付款申请单付讫结算信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pushSrmPayRequestPaid(AggPayrequest aggvo) throws Exception;

	/*
	 * 推送通用付款单申请单
	 */
	public String pushEbsgeneralPayrquest(AggPayrequest aggvo) throws Exception;

	/*
	 * 推送通用付款单
	 */
	public String pushEbsgeneralPay(AggPayBillVO aggvo) throws Exception;

	/**
	 * 付款申请单退回(SRM)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
	public String pushSRMthPayrequest(AggPayrequest aggvo) throws Exception;

	/**
	 * 付款申请单退回(通用)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String pushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * 应付单退回(通用)
	 * 
	 * @param aggvo
	 * @param type
	 * @return
	 * @throws BusinessException
	 */
	public String pushEBSthPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * 付款申请单退回(成本) 2019-12-25-tzj
	 */
	public String costPushEBSthPayrequest(AggPayrequest aggvo)
			throws BusinessException;

	/**
	 * 应付单单退回(成本) 2019-12-25-tzj
	 */
	public String costPushEBSPayablerequest(AggPayableBillVO aggvo,
			AggPayrequest aggVO) throws BusinessException;

	/**
	 * 发票回写 2019-12-25-tzj
	 */
	public String bondtoSRM(AggGatheringBillVO aggvo) throws BusinessException;

	/**
	 * 应付单仅补票推ebs(成本) 2020-04-03-谈子健
	 */
	public String CostInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException;

	/**
	 * 应付单仅补票推ebs(通用) 2020-04-03-谈子健
	 */
	public String TranInvoiceSupplement(AggPayableBillVO aggvo)
			throws BusinessException;

	/**
	 * 开票工单开票 huangxj
	 * 
	 * @throws Exception
	 */
	public String OpenInvoice(HashMap<String, Object> value) throws Exception;

	/**
	 * 占预算+扣税差应付单审批通过推ebs(成本) 2020-06-05-谈子健
	 */
	public String CostBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,String flag)
			throws BusinessException;

	/**
	 * 占预算+扣税差应付单审批通过推ebs(通用) 2020-06-05-谈子健
	 */
	public String TranBudgetTaxdifferencePushEbs(AggPayableBillVO aggvo,String flag)
			throws BusinessException;
}
