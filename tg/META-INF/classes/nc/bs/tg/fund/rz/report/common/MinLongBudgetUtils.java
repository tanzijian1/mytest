package nc.bs.tg.fund.rz.report.common;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.common.MinLongBudgetVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 通用-中长期预算执行台账
 * 
 * @author ASUS
 * 
 */
public class MinLongBudgetUtils extends ReportUtils {
	static MinLongBudgetUtils utils;

	public static MinLongBudgetUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new MinLongBudgetUtils();
		}
		return utils;
	}

	public MinLongBudgetUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance()
				.getPropertiesAry(new MinLongBudgetVO()));
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
			List<MinLongBudgetVO> list = null;

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}
