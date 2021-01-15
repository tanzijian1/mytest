package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.itf.tg.rz.report.TGReprotContst;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.MortgageListVO;

import com.ufida.dataset.IContext;

/**
 * 抵押物清单
 * 
 * @author HUANGDQ
 * @date 2019年7月5日 上午11:36:42
 */
public class MortgageListDataUtils extends ReportUtils {
	static MortgageListDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static MortgageListDataUtils getUtils() {
		if (utils == null) {
			utils = new MortgageListDataUtils();
		}
		return utils;
	}

	public MortgageListDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new MortgageListVO()));
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(TGReprotContst.KEY_QueryScheme);
			 if (conditionVOs == null || conditionVOs.length == 0) {
			 return generateDateset(cmpreportresults, getKeys());
			 }
			initQuery(conditionVOs);

			MortgageListVO[] vos = getMortgageListVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private MortgageListVO[] getMortgageListVOs() throws BusinessException {
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("select tgrz_projectdata.pk_projectdata")
				.append(",tgrz_projectdata.code projectcode")
				.append(", tgrz_projectdata.name projectname")
				.append(",tgrz_projectdata_c.pk_projectdata_c")
				.append(",tgrz_projectdata_c.periodizationname")
				.append(",tgrz_rep_mortgagelist.land_state")
				.append(",tgrz_rep_mortgagelist.land_date")
				.append(",tgrz_rep_mortgagelist.land_undate")
				.append(",tgrz_rep_mortgagelist.land_note")
				.append(", tgrz_rep_mortgagelist.engineering_state")
				.append(",tgrz_rep_mortgagelist.engineering_date")
				.append(",tgrz_rep_mortgagelist.def1 mortgagedetails ")
				.append(",tgrz_rep_mortgagelist.engineering_undate")
				.append(",tgrz_rep_mortgagelist.engineering_note")
				.append(",tgrz_rep_mortgagelist.property_detailed")
				.append(",tgrz_rep_mortgagelist.property_date")
				.append(", tgrz_rep_mortgagelist.property_undate")
				.append(",tgrz_rep_mortgagelist.property_note ")
				.append(",tgrz_rep_mortgagelist.creator")
				.append(",tgrz_rep_mortgagelist.creationtime")
				.append(",tgrz_rep_mortgagelist.modifier")
				.append(",tgrz_rep_mortgagelist.modifiedtime")
				.append(" from tgrz_projectdata ")
				.append(" inner join tgrz_projectdata_c on tgrz_projectdata.pk_projectdata = tgrz_projectdata_c.pk_projectdata ")
				.append(" left join tgrz_rep_mortgagelist on tgrz_projectdata_c.pk_projectdata_c = tgrz_rep_mortgagelist.pk_periodization ")
				.append("  where tgrz_projectdata.dr = 0 and tgrz_projectdata_c.dr = 0 ");
		if (queryValueMap.get("pk_projectdata") != null) {
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn("tgrz_projectdata.pk_projectdata",
							queryValueMap.get("pk_projectdata").split(",")));
		}
		if (queryValueMap.get("pk_projectdata_c") != null) {
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata_c.pk_projectdata_c",
							queryValueMap.get("pk_projectdata_c").split(",")));
		}
		sqlBuff.append(" order by tgrz_projectdata.code,nlssort(tgrz_projectdata_c.pk_projectdata_c) ");
		Collection<MortgageListVO> coll = (Collection<MortgageListVO>) getBaseDAO()
				.executeQuery(sqlBuff.toString(),
						new BeanListProcessor(MortgageListVO.class));

		return coll != null && coll.size() > 0 ? coll
				.toArray(new MortgageListVO[0]) : null;
	}

	/**
	 * 初始化查询条件信息
	 * 
	 * @param condVOs
	 */
	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		for (ConditionVO condVO : condVOs) {
			if ("in".equals(condVO.getOperaCode())) {
				String value = condVO.getValue();
				if (value != null) {
					queryValueMap.put(
							condVO.getFieldCode(),
							value.replace("'", "").replace("(", "")
									.replace(")", ""));
				} else {
					queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
				}
			} else {
				queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
			}
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}
}
