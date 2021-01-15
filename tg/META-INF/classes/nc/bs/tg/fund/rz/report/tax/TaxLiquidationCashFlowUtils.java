package nc.bs.tg.fund.rz.report.tax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.tax.TaxLiquidationCashFlowVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 税收清算支撑-现金流数据表（税务报表）
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationCashFlowUtils extends ReportUtils {
	static TaxLiquidationCashFlowUtils utils;

	public static TaxLiquidationCashFlowUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new TaxLiquidationCashFlowUtils();
		}
		return utils;
	}

	public TaxLiquidationCashFlowUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new TaxLiquidationCashFlowVO()));
	}

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			
			initQuery(conditionVOs);
			List<TaxLiquidationCashFlowVO> list = null;

			list = getCash(conditionVOs);
			
			if (list.size() == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			
			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}