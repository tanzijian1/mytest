package nc.itf.tg.rz.report;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

/**
 * ������Ŀ�������ݲ�ѯ�ӿ�
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����2:55:02
 */
public interface ITGRZReprotDataProcess {
	/**
	 * ������ʷſ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinYearLoanData(IContext context)
			throws BusinessException;

	/**
	 * �������ͷſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinTypeLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * ���ʻ����ſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinOrganizationLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * ���ʻ����ſ�ͳ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinOrganizationTypeLoanTotalData(IContext context)
			throws BusinessException;

	/**
	 * ���������ϸ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinBalanceDetailedData(IContext context)
			throws BusinessException;

	/**
	 * �ѽ���������ϸ
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinClosedDetailedData(IContext context)
			throws BusinessException;

	/**
	 * �¶����ʼƻ����ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinMonthlyPlanTotalData(IContext context)
			throws BusinessException;

	/**
	 * �¶����ʼƻ���
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinMonthlyPlanData(IContext context)
			throws BusinessException;

	/**
	 * 
	 * �������ʳɱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinStockCostData(IContext context)
			throws BusinessException;

	/**
	 * ������ʳɱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinYearCostAccountingData(IContext context)
			throws BusinessException;

	/**
	 * �����ڻ�������ʹ�����
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOrganizationApprovalTotalData(IContext context)
			throws BusinessException;

	/**
	 * �����ڻ����������ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getOrganizationCreditUsageData(IContext context)
			throws BusinessException;

	/**
	 * ���Ϣ���ܱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getRepAndPayTotalData(IContext context)
			throws BusinessException;

	/**
	 * ���Ϣ��
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getRepAndPayData(IContext context) throws BusinessException;

	/**
	 * ��Ѻ���嵥
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getMortgageListData(IContext context)
			throws BusinessException;
	
	/**
	 * ���ʽ��ȱ�
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getFinancingProData(IContext context)
			throws BusinessException;
	
	/**
	 * ����β���ʲ�ѭ�������
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public DataSet getHousePurchaseData(IContext context) throws BusinessException;
}
