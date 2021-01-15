package nc.itf.tg.outside;

import java.util.Map;

import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ResultVO;

public interface IPushBPMLLBillService {
	/**
	 * NC�����˵�ʱ֪ͨӰ��ɾ���ļ�
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void chargebackBillToImg(Map<String, String> map)
			throws BusinessException;

	/**
	 * ��ֹ�������뵥��������
	 * 
	 * @param taskid
	 *            BPMID
	 * @param returnMsg
	 *            ��������ԭ��
	 * @throws BusinessException
	 */
	public void dealChargebackMattApp(String taskid, String returnMsg)
			throws BusinessException;

	/**
	 * ��ֹSRM��������
	 * 
	 * @param taskid
	 * @param returnMsg
	 * @throws BusinessException
	 */
	public void dealChargebackSRMPay(String taskid, String returnMsg)
			throws BusinessException;

	/**
	 * ��ֹ��������
	 * 
	 * @param billid
	 *            ��������
	 * @param returnMsg
	 *            ��������ԭ��
	 * @param pk_tradetype
	 *            ��������
	 * @param isApproved
	 *            �Ƿ�����ͨ��
	 * @throws BusinessException
	 */
	public void dealChargebackBill(Class c, String billid, String returnMsg,
			String pk_tradetype, Boolean isApproved) throws BusinessException;

	/**
	 * ��ֹ������㵥������
	 * 
	 * @param billid
	 *            ��������
	 * @param returnMsg
	 *            ��������ԭ��
	 * @param pk_tradetypeid
	 *            ��������PK
	 * @param isApproveFinish
	 *            �Ƿ���������
	 * @throws BusinessException
	 */
	public void dealChargebackPaybill(String billid, String returnMsg,
			String pk_tradetypeid) throws BusinessException;

	/**
	 * ���ͱ��������ݵ�BPM
	 * 
	 * @param billtype
	 * @param jkbxvo
	 * @throws BusinessException
	 */
	public ResultVO pushBXbillToBpm(String billtype, JKBXVO jkbxvo)
			throws BusinessException;

	/**
	 * ���ͷ������뵥���ݵ�BPM
	 * 
	 * @param billtype
	 * @param aggvo
	 * @throws BusinessException
	 */
	public ResultVO pushMattbillToBpm(String billtype, AggMatterAppVO aggvo)
			throws BusinessException;

	/**
	 * У�鷢Ʊ�Ƿ����ͨ��
	 * 
	 * @param billid
	 *            ��������
	 * @param totalInvMny
	 *            ��ֵ˰��Ʊ�ܽ��
	 * @param totalInvTax
	 *            ��ֵ˰��Ʊ��˰��
	 * @throws BusinessException
	 */
	public void checkInvoiceMsg(String billid, UFDouble totalInvMny,
			UFDouble totalInvTax) throws BusinessException;
}
