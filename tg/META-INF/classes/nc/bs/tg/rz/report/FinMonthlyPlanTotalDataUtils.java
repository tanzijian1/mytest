package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.om.pub.SuperVOHelper;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinMonthlyPlanDetailedVO;
import nc.vo.tg.rz.report.FinMonthlyPlanTotalVO;
import nc.vo.tg.rz.report.FinMonthlyPlanVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 月度融资计划汇总表
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:48:53
 */
public class FinMonthlyPlanTotalDataUtils extends FinMonthlyPlanDataUtils {
	static FinMonthlyPlanTotalDataUtils utils;

	public static FinMonthlyPlanTotalDataUtils getTotalUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new FinMonthlyPlanTotalDataUtils();
		}
		return utils;
	}

	public FinMonthlyPlanTotalDataUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinMonthlyPlanTotalVO()));
		StringBuffer columnSQL = new StringBuffer();
		columnSQL
				.append("pk_finmonthlyplan char(20),")
				.append(" pk_project  char(20),")
				// 项目ID
				.append("pk_periodization char(20), ")
				// 分期主键
				.append("pk_fintype char(20),  ")
				// 融资类型
				.append("monthtype varchar(10), ")
				// 月度类型
				.append("loanmonplan_amount NUMBER(28,8), ")
				// 月度计划放款  
				.append("loanmonadj_amount NUMBER(28,8), ")
				// 月度计划放款(调整数)
				.append("loanmonactual_amount NUMBER(28,8), ")
				// 月度实际放款
				.append("initplan_amount NUMBER(28,8), ")
				// 年度期初计划放款
				.append("initactual_amount NUMBER(28,8), ")
				// 年度期初计划放款(调整数)
				.append("initadj_amount NUMBER(28,8), ")
				// 年度期初实际放款
				.append("dr SMALLINT DEFAULT 0,ts  VARCHAR(19)  DEFAULT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");

		SQLUtil.createTempTable("tgrz_finmonthlyplandetailed",
				columnSQL.toString(), null);
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;		
		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			setTypeMap();// 缓存融资类型信息
			setStandardMap();// 设置当前年的融资标准
			FinMonthlyPlanTotalVO[] vos = getFinMonthlyPlanTotalVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	@SuppressWarnings("unused")
	private FinMonthlyPlanTotalVO[] getFinMonthlyPlanTotalVOs()
			throws BusinessException {
		FinMonthlyPlanVO[] vos = getFinMonthlyPlanVOs();// 读取月度融资计划主体信息
		Map<String, Map<String, UFDouble>> adjMap = getMonthlyAdjlMap();// 获得期间放款调整金额

		Map<String, Map<String, UFDouble>> actualMap = getMonthlyActualMap();// 获得期间放款实发金额
		List<FinMonthlyPlanVO> list = getHandleFinMonthlyPlanVO(vos, adjMap,
				actualMap);
		if (list != null && list.size() > 0) {
			FinMonthlyPlanDetailedVO[] detailedVOs = SuperVOHelper
					.createSuperVOsFromSuperVOs(
							list.toArray(new FinMonthlyPlanVO[0]),
							FinMonthlyPlanDetailedVO.class);
			getBaseDAO().insertVOArray(detailedVOs);
			FinMonthlyPlanTotalVO[] totalVOs = getFinMonthlyPlanTotalVO();
			return totalVOs;
		}

		return null;
	}

	private FinMonthlyPlanTotalVO[] getFinMonthlyPlanTotalVO()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");

		sql.append(" select v1.month ")
				.append(",v1.loanplantotal_amount")
				.append(",v1.loanplantotaladj_amount ")
				.append(",v1.loanactualtotal_amount")
				.append(",v1.loanplan_ratio")
				.append(", case when loanactualtotal_amount = 0 then  0 ")
				.append(" else loanplantotaladj_amount / loanactualtotal_amount end loanplanadj_ratio ")
				.append(",sum(nvl(v1.loanmonplantotal_amount,0)+nvl(v2.mny,0))  over(order by v1.month) loanmonplantotal_amount")
				.append(",sum(nvl(v1.loanplantotaladj_amount,0)+nvl(v2.mny,0))  over(order by v1.month) loanmonplantotaladj_amount")
				.append(",sum(nvl(v1.loanmonactualtotal_amount,0)) over(order by v1.month) loanmonactualtotal_amount")
				.append(",case when (sum(nvl(v1.loanmonactualtotal_amount,0)) over(order by v1.month))=0 then 0 "
						+ "  else "
						+ " (sum(nvl(v1.loanmonplantotal_amount,0)+nvl(v2.mny,0))  over(order by v1.month))/sum(nvl(v1.loanmonactualtotal_amount,0)) over(order by v1.month)"
						+ " end loanmonplan_ratio")
				.append(",case when (sum(nvl(v1.loanmonactualtotal_amount,0)) over(order by v1.month))=0 then 0 "
						+ "  else "
						+ " (sum(nvl(v1.loanplantotaladj_amount,0)+nvl(v2.mny,0))  over(order by v1.month))/sum(nvl(v1.loanmonactualtotal_amount,0)) over(order by v1.month)"
						+ " end loanmonplanadj_ratio")	
				.append(" from (")
				.append("select detailed.monthtype  month ")
				// 月份
				.append(",sum(nvl(loanmonplan_amount,0)) loanplantotal_amount ")
				// 月度计划放款合计
					.append(",sum(nvl((case when loanmonadj_amount is null or loanmonadj_amount = 0 then "
							+ "loanmonplan_amount else loanmonadj_amount end) ,0)) loanplantotaladj_amount ")
				/*.append(",sum(nvl(loanmonplanadj_amount,0)) loanplantotaladj_amount ")
				// 月度计划放款合计(调整数)*/
				.append(",sum(nvl(loanmonactual_amount,0)) loanactualtotal_amount ")
				// 月度实际放款合计
				
			
				// 月度计划放款合计(调整数)
				.append(",case when sum(nvl(loanmonactual_amount,0)) =0 then 0 "
						+ " else sum(nvl(loanmonplan_amount,0))/sum(nvl(loanmonactual_amount,0)) end  loanplan_ratio")
				// 月度融资计划完成率
				.append(",sum(nvl(loanmonplan_amount,0)) loanmonplantotal_amount")
				// 月度计划放款+期初
				.append(",sum(nvl(loanmonactual_amount,0)) loanmonactualtotal_amount")
				// 月度实际放款+期初
				.append(" from tgrz_finmonthlyplandetailed detailed  ")
				.append(" group by detailed.monthtype ")
				.append(")v1")
				.append(" left join (")
				.append(" select t.dyear_change || '-' || to_char(t.dmoon_change, 'fm09') monthtype ")
				.append(",sum(moon_total) mny ")
				.append(" from tgrz_FinancingTotal t ")
				.append(" where t.approvestatus = 1 and t.dyear_change ='"
						+ year + "'")
				.append("  group by t.dyear_change || '-' || to_char(t.dmoon_change, 'fm09') ")
				.append(")v2 on v1.month = v2.monthtype ");
		List<FinMonthlyPlanTotalVO> list = (List<FinMonthlyPlanTotalVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(FinMonthlyPlanTotalVO.class));
		return list != null && list.size() > 0 ? list
				.toArray(new FinMonthlyPlanTotalVO[0]) : null;
	}

	/**
	 * 获得期间实发金额
	 * 
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, Map<String, UFDouble>> getMonthlyActualMap()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");
		sql.append("select pro.pk_projectdata pk_project ")
				// 项目主键
				.append(",prostages.pk_projectdata_c pk_periodization")
				// 项目分期主键
				.append(",to_char(to_date(contexec.busidate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm') datatypename ")
				.append(",cont.pk_rzlx pk_fintype")
				// 月份
				.append(",sum(nvl(contexec.payamount,0)) mny")
				// 年度放款金额(月度实际放款)
				.append(" from tgrz_projectdata pro  ")
				.append(" left join tgrz_FinancingPlan finplan on pro.pk_projectdata = finplan.Pk_Project and finplan.dr = 0 and finplan.dfinancing_year= '"
						+ year + "'")
				.append(" left join tgrz_projectdata_c prostages on prostages.pk_projectdata_c = finplan.project_phases and prostages.dr = 0 ")
				.append(" left join cdm_contract cont on  cont.pk_project= pro.pk_projectdata and cont.dr =0 and cont.vdef3= (case when finplan.project_phases is null then  cont.vdef3 else finplan.project_phases  end )")
				.append(" left join cdm_contract_exec contexec on contexec.pk_contract=cont.pk_contract and contexec.dr =0  and  contexec.summary='FBJ' ")
				.append(" where pro.dr = 0 ");

		if (queryValueMap.get("pk_project") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("pro.pk_projectdata", queryValueMap
							.get("pk_project").split(",")));
		}
		sql.append(" group by pro.pk_projectdata")
				.append(",prostages.pk_projectdata_c")
				.append(",cont.pk_rzlx ")
				.append(",to_char(to_date(contexec.busidate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm')");
		List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		Map<String, Map<String, UFDouble>> actualMap = new HashMap<String, Map<String, UFDouble>>();

		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				String pk_project = (String) map.get("pk_project");
				String pk_periodization = map.get("pk_periodization") == null ? "~"
						: (String) map.get("pk_periodization");
				String key = pk_project + "&" + pk_periodization + "&"
						+ map.get("pk_fintype");
				if (!actualMap.containsKey(key)) {
					actualMap.put(key, new HashMap<String, UFDouble>());
				}
				String datatypename = (String) map.get("datatypename");
				UFDouble mny = new UFDouble(String.valueOf(map.get("mny")));
				if (!actualMap.get(key).containsKey(datatypename)) {
					actualMap.get(key).put(datatypename, UFDouble.ZERO_DBL);
				}
				actualMap.get(key).put(datatypename,
						actualMap.get(key).get(datatypename).add(mny));
			}

		}

		return actualMap;
	}

	/**
	 * 读取月度融资计划VO
	 * 
	 * @return
	 * @throws BusinessException
	 */
	protected FinMonthlyPlanVO[] getFinMonthlyPlanVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");
		sql.append("select pro.pk_projectdata pk_project ")
				// 项目主键
				.append(",pro.projectcode")
				// 项目名称
				.append(",prostages.pk_projectdata_c pk_periodization")
				// 项目分期
				.append(",pro.pk_fintype pk_fintype")
				// 融资类型
				.append(",decode(finplan.nfinancing_money,NULL,pro.nmny,finplan.nfinancing_money ) fin_amount")
				// 融资金额
				.append(",nvl(finplan.nfinancing_plan,0) finyearplan_amount")
				// 年度融资计划金额
				.append(",decode(nvl(moonfinplan.fin_plan,0),NULL, nvl(finplan.nfinancing_plan, 0),nvl(moonfinplan.fin_plan,0)) finyearplanadj_amount ")
				//年度融资计划金额(调整数)
				.append(" from (" + getFinMonthlyPlanMidSQL() + ") pro ")
				.append(" left join tgrz_FinancingPlan finplan on pro.pk_projectdata = finplan.Pk_Project and finplan.dr = 0 and finplan.dfinancing_year= '"
						+ year
						+ "' and finplan.Financing_Type = pro.pk_fintype")
				.append(" left join(select  moonfinplan.project, moonfinplan.financing_type, sum(nvl(moonfinplan.fin_plan, 0))fin_plan  ")
				.append("  from tgrz_MoonFinancingPlan moonfinplan  where  moonfinplan.dr = 0  and moonfinplan.dyear_Change = '"+year+"' ")
				.append(" group  by moonfinplan.project, moonfinplan.financing_type )moonfinplan on pro.pk_projectdata = moonfinplan.project and moonfinplan.financing_type = pro.pk_fintype ")
				.append(" left join tgrz_projectdata_c prostages on prostages.pk_projectdata_c = finplan.project_phases and prostages.dr = 0 ")
				.append("  where 1=1 ");

		List<FinMonthlyPlanVO> list = (List<FinMonthlyPlanVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(FinMonthlyPlanVO.class));

		return list != null && list.size() > 0 ? list
				.toArray(new FinMonthlyPlanVO[0]) : null;
	}
}
