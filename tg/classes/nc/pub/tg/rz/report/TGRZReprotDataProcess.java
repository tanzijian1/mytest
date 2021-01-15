package nc.pub.tg.rz.report;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.rz.report.ITGRZReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * ʱ��������Ŀ�������ݼӹ����������
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����2:50:21
 */
public class TGRZReprotDataProcess {
	/**
	 * ������ʷſ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinYearLoanData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinYearLoanData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �������ͷſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinTypeLoanTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinTypeLoanTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���ʻ����ſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinOrganizationLoanTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess balance = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = balance
				.getFinOrganizationLoanTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���ʻ����ſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinOrganizationTypeLoanTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess balance = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = balance
				.getFinOrganizationTypeLoanTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���������ϸ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinBalanceDetailedData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinBalanceDetailedData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �ѽ���������ϸ
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinClosedDetailedData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinClosedDetailedData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �¶����ʼƻ����ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinMonthlyPlanTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinMonthlyPlanTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �¶����ʼƻ���
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinMonthlyPlanData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinMonthlyPlanData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ������ʳɱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinStockCostData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinStockCostData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �������ʳɱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinYearCostAccountingData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinYearCostAccountingData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �����ڻ�������ʹ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getOrganizationApprovalTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess balance = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = balance
				.getOrganizationApprovalTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * �����ڻ����������ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getOrganizationCreditUsageData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getOrganizationCreditUsageData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���Ϣ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getRepAndPayData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getRepAndPayData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���Ϣ���ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getRepAndPayTotalData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getRepAndPayTotalData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}

	/**
	 * ���Ϣ���ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getMortgageListData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getMortgageListData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}
	
	/**
	 * ���ʽ��ȱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public static DataSet getFinancingProData(IContext context)
			throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getFinancingProData(context);
		setPrecision(resultDataSet);
		return resultDataSet;
	}
	
	/**
	 * ����β���ʲ�ѭ�������
	 * @param context
	 * @return
	 */
	public static DataSet getHousePurchaseData(IContext context) throws BusinessException {
		ITGRZReprotDataProcess process = NCLocator.getInstance().lookup(
				ITGRZReprotDataProcess.class);
		DataSet resultDataSet = process.getHousePurchaseData(context);
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

}
