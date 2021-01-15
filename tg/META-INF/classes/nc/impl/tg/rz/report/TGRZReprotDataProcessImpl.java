package nc.impl.tg.rz.report;

import nc.bs.tg.rz.report.FinBalanceDetailedDataUtils;
import nc.bs.tg.rz.report.FinClosedDetailedDataUtils;
import nc.bs.tg.rz.report.FinMonthlyPlanDataUtils;
import nc.bs.tg.rz.report.FinMonthlyPlanTotalDataUtils;
import nc.bs.tg.rz.report.FinOrganizationLoanTotalDataUtils;
import nc.bs.tg.rz.report.FinOrganizationTypeLoanTotalDataUtils;
import nc.bs.tg.rz.report.FinStockCostDataUtils;
import nc.bs.tg.rz.report.FinTypeLoanTotalDataUtils;
import nc.bs.tg.rz.report.FinYearCostAccountingDataUtils;
import nc.bs.tg.rz.report.FinYearLoanDataUtils;
import nc.bs.tg.rz.report.FinancingProUtils;
import nc.bs.tg.rz.report.HousePurchaseDataUtils;
import nc.bs.tg.rz.report.MortgageListDataUtils;
import nc.bs.tg.rz.report.OrganizationApprovalTotalDataUtils;
import nc.bs.tg.rz.report.OrganizationCreditUsageDataUtils;
import nc.bs.tg.rz.report.RepAndPayDataUtils;
import nc.bs.tg.rz.report.RepAndPayTotalDataUtils;
import nc.itf.tg.rz.report.ITGRZReprotDataProcess;
import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

public class TGRZReprotDataProcessImpl implements ITGRZReprotDataProcess {

	@Override
	public DataSet getFinYearLoanData(IContext context)
			throws BusinessException {

		return FinYearLoanDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinTypeLoanTotalData(IContext context)
			throws BusinessException {
		return FinTypeLoanTotalDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinOrganizationLoanTotalData(IContext context)
			throws BusinessException {
		return FinOrganizationLoanTotalDataUtils.getUtils().getProcessData(
				context);
	}

	@Override
	public DataSet getFinOrganizationTypeLoanTotalData(IContext context)
			throws BusinessException {

		return FinOrganizationTypeLoanTotalDataUtils.getUtils().getProcessData(
				context);
	}

	@Override
	public DataSet getFinBalanceDetailedData(IContext context)
			throws BusinessException {

		return FinBalanceDetailedDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinClosedDetailedData(IContext context)
			throws BusinessException {

		return FinClosedDetailedDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinMonthlyPlanTotalData(IContext context)
			throws BusinessException {

		return FinMonthlyPlanTotalDataUtils.getTotalUtils().getProcessData(
				context);
	}

	@Override
	public DataSet getFinMonthlyPlanData(IContext context)
			throws BusinessException {

		return FinMonthlyPlanDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinStockCostData(IContext context)
			throws BusinessException {
		return FinStockCostDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinYearCostAccountingData(IContext context)
			throws BusinessException {

		return FinYearCostAccountingDataUtils.getUtils()
				.getProcessData(context);
	}

	@Override
	public DataSet getOrganizationApprovalTotalData(IContext context)
			throws BusinessException {

		return OrganizationApprovalTotalDataUtils.getUtils().getProcessData(
				context);
	}

	@Override
	public DataSet getOrganizationCreditUsageData(IContext context)
			throws BusinessException {

		return OrganizationCreditUsageDataUtils.getUtils().getProcessData(
				context);
	}

	@Override
	public DataSet getRepAndPayTotalData(IContext context)
			throws BusinessException {

		return RepAndPayTotalDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getRepAndPayData(IContext context) throws BusinessException {

		return RepAndPayDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getMortgageListData(IContext context)
			throws BusinessException {
		return MortgageListDataUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getFinancingProData(IContext context)
			throws BusinessException {
		return FinancingProUtils.getUtils().getProcessData(context);
	}

	@Override
	public DataSet getHousePurchaseData(IContext context)
			throws BusinessException {
		return HousePurchaseDataUtils.getUtils().getProcessData(context);
	}
}
