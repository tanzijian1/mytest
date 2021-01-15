package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

import com.alibaba.fastjson.JSONObject;

/**
 * 销售系统对接内部处理接口
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncEBSServcie {
	/**
	 * 表单同步
	 * 
	 * @param value
	 * @param billtype
	 * @param srytype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype, String srctype) throws BusinessException;

	/**
	 * 档案信息同步
	 * 
	 * @param value
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String desdoc) throws BusinessException;

	/**
	 * NC付款完成之后调用EBS接口回写付款登记
	 * 
	 * @param settid
	 *            结算单主键
	 * @param code
	 *            交易码
	 * @param syscode
	 *            调用系统
	 * @param token
	 *            验证码
	 * @param postdata
	 *            发布信息
	 * @param registryName
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> onPushEBSPayData_RequiresNew(String settid,
			String code, String syscode, String token, Object postdata,
			String registryName,String pk_paybill) throws BusinessException;

	/**
	 * NC付款完成之后调用EBS接口回写影像发票信息
	 * 
	 * @param settid
	 * @param code
	 * @param syscode
	 * @param token
	 * @param postdata
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> onPushEBSInvData_RequiresNew(String settid,
			String code, String syscode, String token, Object postdata)
			throws BusinessException;

	/**
	 * NC收款完成之后调用EBS接口回写收款登记
	 * 
	 * @param settid
	 *            结算单主键
	 * @param code
	 *            交易码
	 * @param syscode
	 *            调用系统
	 * @param token
	 *            验证码
	 * @param postdata
	 *            发布信息
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> onPushEBSReceivablesData_RequiresNew(
			String settid, String code, String syscode, String token,
			Object postdata, String registryName) throws BusinessException;


	/**
	 * 定时同步执行情况信息至NC系统2020-02-24-谈子健
	 * 
	 * @param aggVO
	 * @param title
	 * @param msg
	 * @throws BusinessException
	 */
	public void syncExecutionDataUpdateExecution_RequiresNew(AggCtApVO aggVO,
			java.lang.String title, java.lang.String msg)
			throws BusinessException;
	
	
	/**
	 * 开票后审批以及更新标识
	 * 
	 * @param aggvo
	 * @param title
	 * @param json
	 * @throws BusinessException
	 */
	public String MakeInvoiceApproveUpdate(AggInvoicingHead aggvo, JSONObject json)
			throws BusinessException;

}
