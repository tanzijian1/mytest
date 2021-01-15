package nc.bs.tg.fund.rz.report.cost;


import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.PledgeLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 押金保证金台账（成本报表）
 * 
 * @author ASUS
 * 
 */
public class PledgeLedgerUtils extends ReportUtils {
	static PledgeLedgerUtils utils;

	public static PledgeLedgerUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new PledgeLedgerUtils();
		}
		return utils;
	}

	public PledgeLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new PledgeLedgerVO()));
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
			List<PledgeLedgerVO> list = null;

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}