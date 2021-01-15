package nc.bs.tg.fund.rz.report;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.fund.rz.report.ITGFundReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * ʱ����Ŀ�������ݼӹ����������
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����2:50:21
 */
public class TGFundReprotDataProcess {
	/**
	 * ����������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getSRMArrivalAcoountData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getSRMArrivalAcoountData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �ݹ��ɱ�������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getSRMEstimateCostDiffData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getSRMEstimateCostDiffData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �ݹ����������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getSRMEstimateRecDiffData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getSRMArrivalAcoountData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ��Ӧ����Ŀ��˾����ɱ�������Ӧ������
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getSRMProOrgAccountData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getSRMArrivalAcoountData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ������ϸ����Ӧ������
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getSRMReqAndPayDetailedData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getSRMReqAndPayDetailedData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ��������������ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getAccountCurrentAnalysisData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getAccountCurrentAnalysisData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * Ѻ��֤��̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getCostLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getCostLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �Ŵ����ʷ��÷�Ʊ��ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinOutlayInvDetailedData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getFinOutlayInvDetailedData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ��ݸԣ����ͬ������ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getGivenOrgContPayDetailedData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getGivenOrgContPayDetailedData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ˮ���Զ����۷�Ʊ��ϸ���ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getHydropowerDeductionInvData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getHydropowerDeductionInvData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * Nc��Ʊ̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getInvLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getInvLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �г�����Ŀ�ɱ�̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getMinAndLongProjectCostLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getMinAndLongProjectCostLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * Ѻ��֤��̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getPledgeLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getPledgeLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ��ĩ���飨�ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getTerminalInspectData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getTerminalInspectData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �ְܷ��ɱ�̨�ˣ��ɱ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getTotalSubPacCostLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getTotalSubPacCostLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ˰�ر�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getTaxControlData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getTaxControlData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ˰������֧��-�ֽ������ݱ�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getTaxLiquidationCashFlowData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getTaxLiquidationCashFlowData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ˰������֧��-�������ݱ�˰�񱨱�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getTaxLiquidationProvisionData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getTaxLiquidationProvisionData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ͨ��-�г���Ԥ��ִ��̨��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getMinLongBudgetData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getMinLongBudgetData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���ʷ�Ʊ̨��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getInvoiceLedgerData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getInvoiceLedgerData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

 
	/**
	 * Ӫ����̨ͬ��
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getMarketInvData(IContext context)
			throws BusinessException{
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		 
		DataSet resultDataSet = dataProcess.getMarketInvData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}
	
	/**
	 * Ӫ����ͬ��Ʊ����̨��
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getInvoicePayData(IContext context)
			throws BusinessException{
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet =dataProcess.getSaleInvPayData(context);
		setPrecision(resultDataSet);
		return resultDataSet; 
	}

	/**
	 * ���ÿ�Ŀ��ϸ�� 
	 * 
	 * @param ljf
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getScheduleofExpensesData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getScheduleofExpensesData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}


	/**
	 * ��ȡҵ���ⲿ������־��Ϣ
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getOutsideLogData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getOutsideLogData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}
	
	/**
	 * Ԥ���ճ�ִ����ϸ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getBudgeExeDetailDate(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess.getBudgeExeDetatilData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	
	
	private static void setPrecision(DataSet resultDataSet) {
		for (Field f : resultDataSet.getMetaData().getFields()) {
			if (f.getDataType() == DataTypeConstant.STRING) {
				f.setPrecision(4000);
			}
		}
	}

	
	/**
	 * Ӫ����������ú�Ʊ��Ȩ��������ϸ
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getMarketingManageAndAccrualData(IContext context)
			throws BusinessException {
		ITGFundReprotDataProcess dataProcess = NCLocator.getInstance().lookup(
				ITGFundReprotDataProcess.class);
		DataSet resultDataSet = dataProcess
				.getMarketingManageAndAccrualData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

}
