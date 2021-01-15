package nc.bs.tg.fund.rz.report;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.fund.rz.report.ITGFundReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.metadata.DataTypeConstant;
import nc.pub.smart.metadata.Field;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * 时代项目报表数据加工处理入口类
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 下午2:50:21
 */
public class TGFundReprotDataProcess {
	/**
	 * 到货入账明细表（供应链报表）
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
	 * 暂估成本差异明细表（供应链报表）
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
	 * 暂估收入差异明细表（供应链报表）
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
	 * 供应链项目公司收入成本报表（供应链报表）
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
	 * 请款付款明细表（供应链报表）
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
	 * 往来账龄分析表（成本报表）
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
	 * 押金保证金台账（成本报表）
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
	 * 信贷融资费用发票明细（成本报表）
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
	 * 东莞裕景合同付款明细（成本报表）
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
	 * 水电自动划扣发票明细（成本报表）
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
	 * Nc发票台账（成本报表）
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
	 * 中长期项目成本台账（成本报表）
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
	 * 押金保证金台账（成本报表）
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
	 * 期末稽查（成本报表）
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
	 * 总分包成本台账（成本报表）
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
	 * 税控表（税务报表）
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
	 * 税收清算支撑-现金流数据表（税务报表）
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
	 * 税收清算支撑-计提数据表（税务报表）
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
	 * 通用-中长期预算执行台账
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
	 * 融资发票台账
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
	 * 营销合同台账
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
	 * 营销合同发票付款台帐
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
	 * 费用科目明细表 
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
	 * 读取业财外部交换日志信息
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
	 * 预算日常执行明细表
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
	 * 营销、管理费用后补票及权责发生制明细
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
