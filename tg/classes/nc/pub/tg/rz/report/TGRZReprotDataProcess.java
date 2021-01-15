package nc.pub.tg.rz.report;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.rz.report.ITGRZReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * 时代融资项目报表数据加工处理入口类
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 下午2:50:21
 */
public class TGRZReprotDataProcess {
	/**
	 * 年度融资放款
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
	 * 融资类型放款统计
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
	 * 融资机构放款统计
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
	 * 融资机构放款统计
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
	 * 融资余额明细表
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
	 * 已结清融资明细
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
	 * 月度融资计划汇总表
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
	 * 月度融资计划表
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
	 * 年度融资成本
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
	 * 存量融资成本
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
	 * 各金融机构授信使用情况
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
	 * 各金融机构报批汇总表
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
	 * 还款付息表
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
	 * 还款付息汇总表
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
	 * 还款付息汇总表
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
	 * 融资进度表
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
	 * 购房尾款资产循环购买表
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
