package nc.bs.tg.fund.rz.report.tax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.tax.TaxLiquidationProvisionVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 税收清算支撑-计提数据表（税务报表）
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationProvisionUtils extends ReportUtils {
	static TaxLiquidationProvisionUtils utils;

	public static TaxLiquidationProvisionUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new TaxLiquidationProvisionUtils();
		}
		return utils;
	}

	public TaxLiquidationProvisionUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new TaxLiquidationProvisionVO()));
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
		    List<TaxLiquidationProvisionVO> list = null;
		    
		    list = getProvisions(conditionVOs);
		    
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