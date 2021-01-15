package nc.itf.tg.rz.report;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * 融资项目报表数据查询接口
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 下午2:55:02
 */
public interface ITGRZReprotDataProcess {
	/**
	 * 年度融资放款表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinYearLoanData(IContext context)
			throws BusinessException;

	/**
	 * 融资类型放款统计
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinTypeLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * 融资机构放款统计
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinOrganizationLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * 融资机构放款统计
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinOrganizationTypeLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * 融资余额明细表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinBalanceDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 已结清融资明细
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinClosedDetailedData(IContext context)
			throws BusinessException;

	/**
	 * 月度融资计划汇总表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinMonthlyPlanTotalData(IContext context)
			throws BusinessException;

	/**
	 * 月度融资计划表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinMonthlyPlanData(IContext context)
			throws BusinessException;

	/**
	 * 
	 * 存量融资成本
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinStockCostData(IContext context)
			throws BusinessException;

	/**
	 * 年度融资成本
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinYearCostAccountingData(IContext context)
			throws BusinessException;

	/**
	 * 各金融机构授信使用情况
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOrganizationApprovalTotalData(IContext context)
			throws BusinessException;

	/**
	 * 各金融机构报批汇总表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOrganizationCreditUsageData(IContext context)
			throws BusinessException;

	/**
	 * 还款付息汇总表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getRepAndPayTotalData(IContext context)
			throws BusinessException;

	/**
	 * 还款付息表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getRepAndPayData(IContext context) throws BusinessException;

	/**
	 * 抵押物清单
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMortgageListData(IContext context)
			throws BusinessException;
	
	/**
	 * 融资进度表
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinancingProData(IContext context)
			throws BusinessException;
	
	/**
	 * 购房尾款资产循环购买表
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getHousePurchaseData(IContext context) throws BusinessException;
}
