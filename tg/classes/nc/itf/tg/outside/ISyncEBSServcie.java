package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.pub.BusinessException;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

import com.alibaba.fastjson.JSONObject;

/**
 * ����ϵͳ�Խ��ڲ�����ӿ�
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncEBSServcie {
	/**
	 * ��ͬ��
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
	 * ������Ϣͬ��
	 * 
	 * @param value
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String desdoc) throws BusinessException;

	/**
	 * NC�������֮�����EBS�ӿڻ�д����Ǽ�
	 * 
	 * @param settid
	 *            ���㵥����
	 * @param code
	 *            ������
	 * @param syscode
	 *            ����ϵͳ
	 * @param token
	 *            ��֤��
	 * @param postdata
	 *            ������Ϣ
	 * @param registryName
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> onPushEBSPayData_RequiresNew(String settid,
			String code, String syscode, String token, Object postdata,
			String registryName,String pk_paybill) throws BusinessException;

	/**
	 * NC�������֮�����EBS�ӿڻ�дӰ��Ʊ��Ϣ
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
	 * NC�տ����֮�����EBS�ӿڻ�д�տ�Ǽ�
	 * 
	 * @param settid
	 *            ���㵥����
	 * @param code
	 *            ������
	 * @param syscode
	 *            ����ϵͳ
	 * @param token
	 *            ��֤��
	 * @param postdata
	 *            ������Ϣ
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> onPushEBSReceivablesData_RequiresNew(
			String settid, String code, String syscode, String token,
			Object postdata, String registryName) throws BusinessException;


	/**
	 * ��ʱͬ��ִ�������Ϣ��NCϵͳ2020-02-24-̸�ӽ�
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
	 * ��Ʊ�������Լ����±�ʶ
	 * 
	 * @param aggvo
	 * @param title
	 * @param json
	 * @throws BusinessException
	 */
	public String MakeInvoiceApproveUpdate(AggInvoicingHead aggvo, JSONObject json)
			throws BusinessException;

}
