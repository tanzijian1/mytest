package nc.itf.tg.fund.rz.report;

import nc.pub.smart.data.DataSet;
import com.ufida.dataset.IContext;
import nc.vo.pub.BusinessException;

/* ҵ��һ�廯�������ݲ�ѯ�ӿ�
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����2:55:02
 */
public interface ITGFundReprotDataProcess {
	/**
	 * ����������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMArrivalAcoountData(IContext context)
			throws BusinessException;

	/**
	 * �ݹ����������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMEstimateRecDiffData(IContext context)
			throws BusinessException;

	/**
	 * ��Ӧ����Ŀ��˾����ɱ�������Ӧ������
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMProOrgAccountData(IContext context)
			throws BusinessException;

	/**
	 * �ݹ��ɱ�������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMEstimateCostDiffData(IContext context)
			throws BusinessException;

	/**
	 * ������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMReqAndPayDetailedData(IContext context)
			throws BusinessException;

	/**
	 * ��������������ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getAccountCurrentAnalysisData(IContext context)
			throws BusinessException;

	/**
	 * Ѻ��֤��̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getCostLedgerData(IContext context) throws BusinessException;

	/**
	 * �Ŵ����ʷ��÷�Ʊ��ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getFinOutlayInvDetailedData(IContext context)
			throws BusinessException;

	/**
	 * ��ݸԣ����ͬ������ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getGivenOrgContPayDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 
	 * ˮ���Զ����۷�Ʊ��ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getHydropowerDeductionInvData(IContext context)
			throws BusinessException;

	/**
	 * Nc��Ʊ̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getInvLedgerData(IContext context) throws BusinessException;

	/**
	 * �г�����Ŀ�ɱ�̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getMinAndLongProjectCostLedgerData(IContext context)
			throws BusinessException;

	/**
	 * Ѻ��֤��̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getPledgeLedgerData(IContext context)
			throws BusinessException;

	/**
	 * ��ĩ���飨�ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTerminalInspectData(IContext context)
			throws BusinessException;

	/**
	 * �ְܷ��ɱ�̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTotalSubPacCostLedgerData(IContext context)
			throws BusinessException;

	/**
	 * ˰�ر�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxControlData(IContext context) throws BusinessException;

	/**
	 * ˰������֧��-�������ݱ�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxLiquidationCashFlowData(IContext context)
			throws BusinessException;

	/**
	 * ˰������֧��-�ֽ������ݱ�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxLiquidationProvisionData(IContext context)
			throws BusinessException;

	/**
	 * ͨ��-�г���Ԥ��ִ��̨��
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getMinLongBudgetData(IContext context)
			throws BusinessException;

	/**
	 * ���ʷ�Ʊ̨��
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getInvoiceLedgerData(IContext context)
			throws BusinessException;
	
	/**
	 * Ԥ���ճ�ִ����ϸ��
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getBudgeExeDetatilData(IContext context)
			throws BusinessException;
	/**
	 * ���ÿ�Ŀ��ϸ�� 
	 * 
	 * @param ljf
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getScheduleofExpensesData(IContext context) 
			throws BusinessException;

	/**
	 * ��ȡҵ���ⲿ������־��Ϣ
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOutsideLogData(IContext context) throws BusinessException;
   
	/**
	 * Ӫ����ͬ��Ʊ̨��
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMarketInvData(IContext context) throws BusinessException;
	

	/**
	 * Ӫ����ͬ��Ʊ����̨��
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getSaleInvPayData(IContext context) throws BusinessException;

	/**
	 * ��ѯӪ����������ú�Ʊ��Ȩ��������ϸ
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMarketingManageAndAccrualData(IContext context) throws BusinessException;


}
