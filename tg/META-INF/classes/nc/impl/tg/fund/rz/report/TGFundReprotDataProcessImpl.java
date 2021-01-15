package nc.impl.tg.fund.rz.report;

import nc.bs.tg.fund.rz.report.common.MinLongBudgetUtils;
import nc.bs.tg.fund.rz.report.cost.AccountCurrentAnalysisUtils;
import nc.bs.tg.fund.rz.report.cost.BudgetExeDetailsUtils;
import nc.bs.tg.fund.rz.report.cost.CostLedgerUtils;
import nc.bs.tg.fund.rz.report.cost.FinOutlayInvDetailedUtils;
import nc.bs.tg.fund.rz.report.cost.GivenOrgContPayDetailedUtils;
import nc.bs.tg.fund.rz.report.cost.HydropowerDeductionInvUtils;
import nc.bs.tg.fund.rz.report.cost.InvLedgerUtils;
import nc.bs.tg.fund.rz.report.cost.MarketingManageAndAccrualUtils;
import nc.bs.tg.fund.rz.report.cost.MinAndLongProjectCostLedgerUtils;
import nc.bs.tg.fund.rz.report.cost.PledgeLedgerUtils;
import nc.bs.tg.fund.rz.report.cost.TerminalInspectUtils;
import nc.bs.tg.fund.rz.report.cost.TotalSubPacCostLedgerUtils;
import nc.bs.tg.fund.rz.report.financing.InvoiceLedgerUtils;
import nc.bs.tg.fund.rz.report.financing.ScheduleofExpensesUtils;
import nc.bs.tg.fund.rz.report.outsidelog.OutsideLogUtils;
import nc.bs.tg.fund.rz.report.sale.MarketInvUtils;
import nc.bs.tg.fund.rz.report.sale.SaleInvPayUtils;
import nc.bs.tg.fund.rz.report.srm.SRMArrivalAcoountUtils;
import nc.bs.tg.fund.rz.report.srm.SRMEstimateCostDiffUtils;
import nc.bs.tg.fund.rz.report.srm.SRMEstimateRecDiffUtils;
import nc.bs.tg.fund.rz.report.srm.SRMProOrgAccountUtils;
import nc.bs.tg.fund.rz.report.srm.SRMReqAndPayDetailedUtils;
import nc.bs.tg.fund.rz.report.tax.TaxControlUtils;
import nc.bs.tg.fund.rz.report.tax.TaxLiquidationCashFlowUtils;
import nc.bs.tg.fund.rz.report.tax.TaxLiquidationProvisionUtils;
import nc.itf.tg.fund.rz.report.ITGFundReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

public class TGFundReprotDataProcessImpl implements ITGFundReprotDataProcess {

	@Override
	public DataSet getSRMArrivalAcoountData(IContext context)
			throws BusinessException {

		return SRMArrivalAcoountUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getSRMEstimateRecDiffData(IContext context)
			throws BusinessException {
		return SRMEstimateRecDiffUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getSRMProOrgAccountData(IContext context)
			throws BusinessException {
		return SRMProOrgAccountUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getSRMEstimateCostDiffData(IContext context)
			throws BusinessException {
		return SRMEstimateCostDiffUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getSRMReqAndPayDetailedData(IContext context)
			throws BusinessException {
		return SRMReqAndPayDetailedUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getAccountCurrentAnalysisData(IContext context)
			throws BusinessException {
		return AccountCurrentAnalysisUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getCostLedgerData(IContext context) throws BusinessException {
		return CostLedgerUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinOutlayInvDetailedData(IContext context)
			throws BusinessException {
		return FinOutlayInvDetailedUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getGivenOrgContPayDetailedData(IContext context)
			throws BusinessException {
		return GivenOrgContPayDetailedUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getHydropowerDeductionInvData(IContext context)
			throws BusinessException {
		return HydropowerDeductionInvUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getInvLedgerData(IContext context) throws BusinessException {
		return InvLedgerUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getMinAndLongProjectCostLedgerData(IContext context)
			throws BusinessException {
		return MinAndLongProjectCostLedgerUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getPledgeLedgerData(IContext context)
			throws BusinessException {
		return PledgeLedgerUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getTerminalInspectData(IContext context)
			throws BusinessException {
		return TerminalInspectUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getTotalSubPacCostLedgerData(IContext context)
			throws BusinessException {
		return TotalSubPacCostLedgerUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getTaxControlData(IContext context) throws BusinessException {
		return TaxControlUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getTaxLiquidationCashFlowData(IContext context)
			throws BusinessException {
		return TaxLiquidationCashFlowUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getTaxLiquidationProvisionData(IContext context)
			throws BusinessException {
		return TaxLiquidationProvisionUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getMinLongBudgetData(IContext context)
			throws BusinessException {
		return MinLongBudgetUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getInvoiceLedgerData(IContext context)
			throws BusinessException {
		return InvoiceLedgerUtils.getUtils().getProcessData(context);

	}
	public DataSet getScheduleofExpensesData(IContext context)
			throws BusinessException {
		return ScheduleofExpensesUtils.getUtils().getProcessData(context);

	}

	@Override
	public DataSet getOutsideLogData(IContext context) throws BusinessException {
		return OutsideLogUtils.getUtils().getProcessData(context);
	}


	@Override
	public DataSet getMarketInvData(IContext context) throws BusinessException {
		// TODO 自动生成的方法存根
		return MarketInvUtils.getMarketInvUtils().getProcessData(context);
	}

	@Override
	public DataSet getBudgeExeDetatilData(IContext context)
			throws BusinessException {
		return BudgetExeDetailsUtils.getUtils().getProcessData(context);
	}


	@Override
	public DataSet getSaleInvPayData(IContext context) throws BusinessException {
		// TODO 自动生成的方法存根
		
		return SaleInvPayUtils.getSaleInvPayUtils().getProcessData(context);
	}



	@Override
	public DataSet getMarketingManageAndAccrualData(IContext context)
			throws BusinessException {
		return MarketingManageAndAccrualUtils.getUtils().getProcessData(context);
	}
	

}
