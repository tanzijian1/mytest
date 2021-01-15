package nc.bs.tg.fund.rz.report.cost;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.cost.MinAndLongProjectCostLedgerVO;
import nc.vo.tgfn.report.cost.TotalSubPacCostLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 总分包成本台账（成本报表） ljf
 * 
 * @author ASUS
 * 
 */
public class TotalSubPacCostLedgerUtils extends
		MinAndLongProjectCostLedgerUtils {
	static TotalSubPacCostLedgerUtils utils;
	String voucher = "";

	public static TotalSubPacCostLedgerUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new TotalSubPacCostLedgerUtils();
		}
		return utils;
	}

	public TotalSubPacCostLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new TotalSubPacCostLedgerVO()));
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
			List<MinAndLongProjectCostLedgerVO> list = getCosttLedgerDatas(true);
			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}