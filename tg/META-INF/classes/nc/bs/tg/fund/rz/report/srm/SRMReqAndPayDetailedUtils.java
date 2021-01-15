package nc.bs.tg.fund.rz.report.srm;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.srm.SRMReqAndPayDetailedVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 请款付款明细表（供应链报表）
 * 
 * @author ASUS
 * 
 */
public class SRMReqAndPayDetailedUtils extends ReportUtils {
	static SRMReqAndPayDetailedUtils utils;

	public static SRMReqAndPayDetailedUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new SRMReqAndPayDetailedUtils();
		}
		return utils;
	}

	public SRMReqAndPayDetailedUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new SRMReqAndPayDetailedVO()));
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
			List<SRMReqAndPayDetailedVO> list = null;

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

}