package nc.bs.tg.fund.rz.report.cost;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.AccountCurrentAnalysisVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 往来账龄分析表（成本报表）
 * 
 * @author ASUS
 * 
 */
public class AccountCurrentAnalysisUtils extends ReportUtils {
	static AccountCurrentAnalysisUtils utils;

	public static AccountCurrentAnalysisUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new AccountCurrentAnalysisUtils();
		}
		return utils;
	}

	public AccountCurrentAnalysisUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance()
				.getPropertiesAry(new AccountCurrentAnalysisVO()));
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
			List<AccountCurrentAnalysisVO> list = null;

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}