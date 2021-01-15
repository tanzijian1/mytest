package nc.bs.tg.fund.rz.report.outsidelog;

import java.util.List;

import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 外部交互接口日志
 * 
 * @author ASUS
 * 
 */
public class OutsideLogUtils extends ReportUtils {
	static OutsideLogUtils utils;

	public static OutsideLogUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new OutsideLogUtils();
		}
		return utils;
	}

	public OutsideLogUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new OutsideLogVO()));
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
			List<OutsideLogVO> list = getOutsideLogVOs(conditionVOs);

			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());

	}

	/**
	 * 日志信息
	 * 
	 * @param conditionVOs
	 * @return
	 * @throws BusinessException
	 */
	private List<OutsideLogVO> getOutsideLogVOs(ConditionVO[] conditionVOs)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select tg_outsidelog.pk_log ")
				.append(", tg_outsidelog.srcsystem")
				.append(",dbms_lob.substr(tg_outsidelog.srcparm,1500) srcparm")
				.append(",tg_outsidelog.desbill")
				.append(",case when tg_outsidelog.result=0 then '失败' else '成功' end result ")
				.append(",dbms_lob.substr(tg_outsidelog.errmsg,1500) errmsg ")
				.append(",tg_outsidelog.exedate").append(",tg_outsidelog.ts ")
				.append("  from tg_outsidelog   ").append(" where 1=1 ");
		if (conditionVOs != null && conditionVOs.length > 0) {
			String wheresql = new ConditionVO().getWhereSQL(conditionVOs);
			if (wheresql != null && !"".equals(wheresql)) {
				sql.append(" and "
						+ new ConditionVO().getWhereSQL(conditionVOs));
			}
		}
		sql.append(" order by ts desc ");
		List<OutsideLogVO> list = (List<OutsideLogVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(OutsideLogVO.class));
		return list;
	}

}
