package nc.itf.tg.fund.rz.report;

import nc.pub.smart.data.DataSet;
import com.ufida.dataset.IContext;
import nc.vo.pub.BusinessException;

/* 业财一体化报表数据查询接口
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 下午2:55:02
 */
public interface ITGFundReprotDataProcess {
	/**
	 * 到货入账明细表（供应链报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMArrivalAcoountData(IContext context)
			throws BusinessException;

	/**
	 * 暂估收入差异明细表（供应链报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMEstimateRecDiffData(IContext context)
			throws BusinessException;

	/**
	 * 供应链项目公司收入成本报表（供应链报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMProOrgAccountData(IContext context)
			throws BusinessException;

	/**
	 * 暂估成本差异明细表（供应链报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMEstimateCostDiffData(IContext context)
			throws BusinessException;

	/**
	 * 请款付款明细表（供应链报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getSRMReqAndPayDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 往来账龄分析表（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getAccountCurrentAnalysisData(IContext context)
			throws BusinessException;

	/**
	 * 押金保证金台账（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getCostLedgerData(IContext context) throws BusinessException;

	/**
	 * 信贷融资费用发票明细（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getFinOutlayInvDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 东莞裕景合同付款明细（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getGivenOrgContPayDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 
	 * 水电自动划扣发票明细（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getHydropowerDeductionInvData(IContext context)
			throws BusinessException;

	/**
	 * Nc发票台账（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getInvLedgerData(IContext context) throws BusinessException;

	/**
	 * 中长期项目成本台账（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getMinAndLongProjectCostLedgerData(IContext context)
			throws BusinessException;

	/**
	 * 押金保证金台账（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getPledgeLedgerData(IContext context)
			throws BusinessException;

	/**
	 * 期末稽查（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTerminalInspectData(IContext context)
			throws BusinessException;

	/**
	 * 总分包成本台账（成本报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTotalSubPacCostLedgerData(IContext context)
			throws BusinessException;

	/**
	 * 税控表（税务报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxControlData(IContext context) throws BusinessException;

	/**
	 * 税收清算支撑-计提数据表（税务报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxLiquidationCashFlowData(IContext context)
			throws BusinessException;

	/**
	 * 税收清算支撑-现金流数据表（税务报表）
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getTaxLiquidationProvisionData(IContext context)
			throws BusinessException;

	/**
	 * 通用-中长期预算执行台账
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getMinLongBudgetData(IContext context)
			throws BusinessException;

	/**
	 * 融资发票台账
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getInvoiceLedgerData(IContext context)
			throws BusinessException;
	
	/**
	 * 预算日常执行明细表
	 * 
	 * @param context
	 * @return
	 */
	public DataSet getBudgeExeDetatilData(IContext context)
			throws BusinessException;
	/**
	 * 费用科目明细表 
	 * 
	 * @param ljf
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getScheduleofExpensesData(IContext context) 
			throws BusinessException;

	/**
	 * 读取业财外部交换日志信息
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOutsideLogData(IContext context) throws BusinessException;
   
	/**
	 * 营销合同发票台账
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMarketInvData(IContext context) throws BusinessException;
	

	/**
	 * 营销合同发票付款台帐
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getSaleInvPayData(IContext context) throws BusinessException;

	/**
	 * 查询营销、管理费用后补票及权责发生制明细
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMarketingManageAndAccrualData(IContext context) throws BusinessException;


}
