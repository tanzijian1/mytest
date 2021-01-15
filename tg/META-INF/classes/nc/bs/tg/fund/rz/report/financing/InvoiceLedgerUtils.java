package nc.bs.tg.fund.rz.report.financing;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.financing.InvoiceLedgerVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * ÈÚ×Ê·¢Æ±Ì¨ÕË
 * 
 * @author ASUS
 * 
 */
public class InvoiceLedgerUtils extends ReportUtils {
	static InvoiceLedgerUtils utils;

	public static InvoiceLedgerUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new InvoiceLedgerUtils();
		}
		return utils;
	}

	public InvoiceLedgerUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new InvoiceLedgerVO()));
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
			List<InvoiceLedgerVO> list = null;

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}