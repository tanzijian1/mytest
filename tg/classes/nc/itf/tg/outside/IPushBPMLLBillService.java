package nc.itf.tg.outside;

import java.util.Map;

import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ResultVO;

public interface IPushBPMLLBillService {
	/**
	 * NC单据退单时通知影像删除文件
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void chargebackBillToImg(Map<String, String> map)
			throws BusinessException;

	/**
	 * 终止费用申请单单据流程
	 * 
	 * @param taskid
	 *            BPMID
	 * @param returnMsg
	 *            撤销流程原因
	 * @throws BusinessException
	 */
	public void dealChargebackMattApp(String taskid, String returnMsg)
			throws BusinessException;

	/**
	 * 终止SRM请款单据流程
	 * 
	 * @param taskid
	 * @param returnMsg
	 * @throws BusinessException
	 */
	public void dealChargebackSRMPay(String taskid, String returnMsg)
			throws BusinessException;

	/**
	 * 终止单据流程
	 * 
	 * @param billid
	 *            单据主键
	 * @param returnMsg
	 *            撤销流程原因
	 * @param pk_tradetype
	 *            交易类型
	 * @param isApproved
	 *            是否审批通过
	 * @throws BusinessException
	 */
	public void dealChargebackBill(Class c, String billid, String returnMsg,
			String pk_tradetype, Boolean isApproved) throws BusinessException;

	/**
	 * 终止付款结算单据流程
	 * 
	 * @param billid
	 *            单据主键
	 * @param returnMsg
	 *            撤销流程原因
	 * @param pk_tradetypeid
	 *            交易类型PK
	 * @param isApproveFinish
	 *            是否审批结束
	 * @throws BusinessException
	 */
	public void dealChargebackPaybill(String billid, String returnMsg,
			String pk_tradetypeid) throws BusinessException;

	/**
	 * 推送报销单数据到BPM
	 * 
	 * @param billtype
	 * @param jkbxvo
	 * @throws BusinessException
	 */
	public ResultVO pushBXbillToBpm(String billtype, JKBXVO jkbxvo)
			throws BusinessException;

	/**
	 * 推送费用申请单数据到BPM
	 * 
	 * @param billtype
	 * @param aggvo
	 * @throws BusinessException
	 */
	public ResultVO pushMattbillToBpm(String billtype, AggMatterAppVO aggvo)
			throws BusinessException;

	/**
	 * 校验发票是否核验通过
	 * 
	 * @param billid
	 *            单据主键
	 * @param totalInvMny
	 *            增值税发票总金额
	 * @param totalInvTax
	 *            增值税发票总税额
	 * @throws BusinessException
	 */
	public void checkInvoiceMsg(String billid, UFDouble totalInvMny,
			UFDouble totalInvTax) throws BusinessException;
}
