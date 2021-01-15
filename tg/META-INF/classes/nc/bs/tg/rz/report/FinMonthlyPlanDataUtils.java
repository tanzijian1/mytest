package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.cmp.tools.UFDoubleUtils;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinMonthlyPlanVO;
import nc.vo.tg.standard.AggStandardVO;
import nc.vo.tg.standard.StandardBVO;
import nc.vo.tg.standard.StandardVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * �¶����ʼƻ���
 * 
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:49:13
 */
public class FinMonthlyPlanDataUtils extends ReportUtils {
	static FinMonthlyPlanDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql
	Map<String, Object> standardMap = new HashMap<String, Object>();
	Map<String, Map<String, String>> typeMap = new HashMap<String, Map<String, String>>();//
	IMDPersistenceQueryService queryService = null;

	public static FinMonthlyPlanDataUtils getUtils() throws BusinessException {
		if (utils == null) {
			utils = new FinMonthlyPlanDataUtils();
		}
		return utils;
	}

	public FinMonthlyPlanDataUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinMonthlyPlanVO()));
		StringBuffer columnSQL = new StringBuffer();
		columnSQL
				.append("pk_finmonthlyplan char(20),")
				.append(" pk_project  char(20),")
				// ��ĿID
				.append("  projectcode  varchar(200),")
				// ��Ŀ����
				.append("projectname varchar(200),")
				// ��Ŀ����

				.append("pk_periodization char(20), ")
				// ��������
				.append("periodizationname varchar(200), ")
				// ��������
				.append("pk_fintype char(20),  ")
				// ��������
				.append("fintypecode varchar(200),  ")
				// �������ͱ���
				.append("fintypename varchar(200),")
				// ������������
				.append("fin_amount NUMBER(28,8),")
				// ���ʽ��
				.append("finyearplan_amount NUMBER(28,8),")
				// ��ȼƻ����ʶ�
				.append("loanyear_amount NUMBER(28,8),")
				// ��ȷſ���
				.append("loanyeartotal_amount NUMBER(28,8),")
				// �ۼƷſ���
				.append("loandate varchar(4000),")
				// �ſ�ʱ��
				.append("notloan_amount NUMBER(28,8),")
				// δ�ſ���
				.append("note varchar(4000), ")
				// ��ע
				.append("fourcardsdate char(19), ")
				// ��֤��ȫʱ��
				.append("landpaydate char(19),")
				// �ؼ����֧��ʱ��
				.append("threecardsdate char(19), ")
				// ��֤��ȫʱ��
				.append("paymentdate char(19),")
				// �Լ�40%֧��ʱ��
				.append("monthtype varchar(10), ")
				// �¶�����
				.append("loanmonplan_amount NUMBER(28,8), ")
				// �¶ȼƻ��ſ�
				.append("loanmonactual_amount NUMBER(28,8), ")
				// �¶�ʵ�ʷſ�
				.append("loanmonadj_amount NUMBER(28,8),")
				// �¶ȵ����ſ�
				.append("initplan_amount NUMBER(28,8), ")
				// ����ڳ��ƻ��ſ�
				.append("initactual_amount NUMBER(28,8), ")
				// ����ڳ�ʵ�ʷſ�
				.append("initadj_amount NUMBER(28,8),")
				// ����ڳ������ſ�
				.append("loanmondiff_amount NUMBER(28,8),")
				// �¶��ۼƲ��"
				.append("dr SMALLINT DEFAULT 0,ts  VARCHAR(19)  DEFAULT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')");

		SQLUtil.createTempTable("tgrz_finmonthlyplan", columnSQL.toString(),
				null);
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
			setTypeMap();// ��������������Ϣ
			setStandardMap();// ���õ�ǰ������ʱ�׼
			Map<String, Map<String, UFDouble>> actualMap = getMonthlyActualMap();// ����ڼ�ſ�ʵ�����
			Map<String, Map<String, UFDouble>> adjMap = getMonthlyAdjlMap();// ����ڼ�ſ�������
			FinMonthlyPlanVO[] vos = getFinMonthlyPlanVOs();// ��ȡ�¶����ʼƻ�������Ϣ
			List<FinMonthlyPlanVO> list = getHandleFinMonthlyPlanVO(vos,
					adjMap, actualMap);

			// // add by huangdq 201200204 ��ȼƻ��������ȡ1~12�»����� -start-
			// Map<String, UFDouble> planMnyMap = new HashMap<String,
			// UFDouble>();
			// if (list != null && list.size() > 0) {
			// for (FinMonthlyPlanVO vo : list) {
			// String key = vo.getPk_project()
			// + "&"
			// + (vo.getPk_periodization() != null ? "~" : vo
			// .getPk_periodization()) + "&"
			// + vo.getPk_fintype();
			// if (!planMnyMap.containsKey(key)) {
			// planMnyMap.put(key, UFDouble.ZERO_DBL);
			// }
			// UFDouble finyearplan_amount = planMnyMap
			// .get(key)
			// .add(vo.getLoanmonplan_amount() == null ? UFDouble.ZERO_DBL
			// : vo.getLoanmonplan_amount());
			// planMnyMap.put(key, finyearplan_amount);
			//
			// }
			// for (FinMonthlyPlanVO vo : list) {
			// String key = vo.getPk_project()
			// + "&"
			// + (vo.getPk_periodization() != null ? "~" : vo
			// .getPk_periodization()) + "&"
			// + vo.getPk_fintype();
			// if (!planMnyMap.get(key).equals(UFDouble.ZERO_DBL)) {
			// vo.setFinyearplan_amount(planMnyMap.get(key));
			// }
			//
			// }
			//
			// }
			// // add by huangdq 201200204 ��ȼƻ��������ȡ1~12�»����� -end-

			getBaseDAO().insertVOArray(list.toArray(new FinMonthlyPlanVO[0]));

			list = getFinMonthlyPlanList();
			if (list != null && list.size() > 0) {
				cmpreportresults = transReportResult(list);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	protected List<FinMonthlyPlanVO> getHandleFinMonthlyPlanVO(
			FinMonthlyPlanVO[] vos, Map<String, Map<String, UFDouble>> adjMap,
			Map<String, Map<String, UFDouble>> actualMap)
			throws BusinessException {
		String year = queryValueMap.get("year");
		Map<String, FinMonthlyPlanVO> finMap = new HashMap<String, FinMonthlyPlanVO>();
		if (vos != null && vos.length > 0) {
			for (FinMonthlyPlanVO vo : vos) {
				// UFDouble loanyeartotal_amount = UFDouble.ZERO_DBL;

				String pk_project = vo.getPk_project();
				String pk_periodization = vo.getPk_periodization() == null ? "~"
						: vo.getPk_periodization();
				String pk_fintype = vo.getPk_fintype();
				String key = pk_project + "&" + pk_periodization + "&"
						+ pk_fintype;
				Map<String, String> finstypeMap = typeMap.get(pk_fintype);
				if (finstypeMap != null) {
					vo.setFintypecode(finstypeMap.get("code"));
					vo.setFintypename(finstypeMap.get("name"));
					if (vo.getPk_fintype() != null) {
						vo = setMonthlyPlanDate(vo);
					}
				}
				Map<String, UFDouble> actualMnyMap = actualMap.get(key);
				Map<String, UFDouble> adjMapMap = adjMap == null ? null
						: adjMap.get(key);
				String inityearmonth = year + "-01";
				for (int i$ = 1; i$ <= 12; i$++) {
					FinMonthlyPlanVO actualVO = (FinMonthlyPlanVO) vo.clone();
					String yearmonth = year + "-" + String.format("%02d", i$);
					actualVO.setMonthtype(yearmonth);

					// ��ȡʵ��������ڳ�
					if (actualMnyMap != null && actualMnyMap.size() > 0) {
						UFDouble initmny = UFDouble.ZERO_DBL;
						if (yearmonth.equals(inityearmonth)) {
							for (String datekey : actualMnyMap.keySet()) {
								if (datekey == null) {
									continue;
								}
								if (UFDate.getDate(inityearmonth + "-01")
										.after(UFDate.getDate(datekey + "-01"))) {
									initmny = UFDoubleUtils.add(initmny,
											actualMnyMap.get(datekey));
								}

							}

							actualVO.setInitactual_amount(initmny);

						}
						if (actualMnyMap.containsKey(yearmonth)) {
							actualVO.setLoanmonactual_amount(actualMnyMap
									.get(yearmonth));
						}
					}

					// ��ȡ����������ڳ�
					if (adjMapMap != null && adjMapMap.size() > 0) {
						UFDouble initmny = UFDouble.ZERO_DBL;
						if (yearmonth.equals(inityearmonth)) {
							for (String datekey : adjMapMap.keySet()) {
								if (datekey == null) {
									continue;
								}
								if (UFDate.getDate(inityearmonth + "-01")
										.after(UFDate.getDate(datekey + "-01"))) {
									initmny = UFDoubleUtils.add(initmny,
											adjMapMap.get(datekey));
								}

							}
							actualVO.setInitadj_amount(initmny);

						}
						if (adjMapMap.containsKey(yearmonth)) {
							actualVO.setLoanmonadj_amount(adjMapMap
									.get(yearmonth));
						}

					}

					finMap.put(key + yearmonth, actualVO);
				}

			}

		}

		for (String key : finMap.keySet()) {
			finMap.put(key, getPlanVO(finMap.get(key)));
		}

		List<FinMonthlyPlanVO> list = new ArrayList<FinMonthlyPlanVO>();
		for (String key : finMap.keySet()) {
			list.add(finMap.get(key));
		}
		return list;
	}

	protected List<FinMonthlyPlanVO> getFinMonthlyPlanList()
			throws BusinessException {
		// add by huangdq 201200204 ��ȼƻ��������ȡ1~12�»����� -start-

		// String sql =
		// " select n.pk_finmonthlyplan, n.pk_project,n.projectcode, n.projectname,n.pk_periodization,n.periodizationname "
		// +
		// ",n.pk_fintype,n.fintypecode, n.fintypename, n.fin_amount,n.finyearplan_amount, n.loanyear_amount,n.loanyeartotal_amount, n.loandate "
		// +
		// ",n.notloan_amount,n.note, n.fourcardsdate, n.landpaydate,n.threecardsdate,n.paymentdate,n.monthtype "
		// +
		// ", n.loanmonplan_amount, n.loanmonactual_amount,n.loanmonadj_amount, n.initplan_amount,n.initactual_amount,n.initadj_amount"
		// +
		// " ,sum(nvl(initplan_amount,0)+nvl(initadj_amount,0)+nvl(loanmonplan_amount,0)+nvl(loanmonadj_amount,0)-nvl(loanmonactual_amount,0)-nvl(initactual_amount,0)) "
		// +
		// " over(partition by pk_project, pk_periodization, pk_fintype order by pk_project,pk_periodization,pk_fintype,monthtype) loanmondiff_amount  "
		// + " from tgrz_finmonthlyplan n ";
		// case when v.finyearplan_amount = 0 then n.finyearplan_amount else
		// v.finyearplan_amount end finyearplan_amount
		String sql = " select n.pk_finmonthlyplan, n.pk_project,n.projectcode,n.projectname,n.pk_periodization,n.periodizationname "
				+ ",n.pk_fintype,n.fintypecode, n.fintypename, n.fin_amount"
				+ ", v.finyearplan_amount finyearplan_amount"
				+ ", case when p.finyearplanadj_amount = 0 then v.finyearplan_amount else p.finyearplanadj_amount   end finyearplanadj_amount"
				+ ", n.loanyear_amount,n.loanyeartotal_amount, n.loandate "
				+ ",n.notloan_amount,n.note, substr(n.fourcardsdate,1,10) fourcardsdate, substr(n.landpaydate,1,10) landpaydate,substr(n.threecardsdate,1,10) threecardsdate,substr(n.paymentdate,1,10) paymentdate,n.monthtype "
				+ ", n.loanmonplan_amount, n.loanmonactual_amount,n.loanmonadj_amount, n.initplan_amount,n.initactual_amount,n.initadj_amount"
				+ " ,sum(nvl(initplan_amount,0)+nvl(initadj_amount,0)+nvl(loanmonplan_amount,0)+nvl(loanmonadj_amount,0)-nvl(loanmonactual_amount,0)-nvl(initactual_amount,0)) "
				+ " over(partition by n.pk_project, n.pk_periodization, n.pk_fintype order by n.pk_project,n.pk_periodization,n.pk_fintype,n.monthtype) loanmondiff_amount  "
				+ " from tgrz_finmonthlyplan n "
				+ " left join (select pk_project,pk_periodization,pk_fintype,sum(nvl(loanmonplan_amount,0)) finyearplan_amount from tgrz_finmonthlyplan plan  group by pk_project,pk_periodization,pk_fintype  ) v "
				+ " on n.pk_project = v.pk_project and nvl(n.pk_periodization,1) = nvl(v.pk_periodization,1) and  n.pk_fintype = v.pk_fintype "
				+ " left join (select pk_project,pk_periodization,pk_fintype, sum(nvl((case when loanmonadj_amount is null  then loanmonplan_amount else loanmonadj_amount end ),0)) finyearplanadj_amount "
				+ " from tgrz_finmonthlyplan plan group by  pk_project,pk_periodization, pk_fintype) p on n.pk_project = p.pk_project "
				+ "  and nvl(n.pk_periodization,1) = nvl(p.pk_periodization,1) and n.pk_fintype = p.pk_fintype  "
				+ " where "
				+ "  (v.finyearplan_amount > 0 and v.finyearplan_amount is not null) "
				+ "  or  (p.finyearplanadj_amount > 0 and  p.finyearplanadj_amount is not null) "
				+ "  or  (n.loanyear_amount > 0 and  n.loanyear_amount is not null)  "
				
				
				;

		// add by huangdq 201200204 ��ȼƻ��������ȡ1~12�»����� -end-

		List<FinMonthlyPlanVO> list = (List<FinMonthlyPlanVO>) getBaseDAO()
				.executeQuery(sql,
						new BeanListProcessor(FinMonthlyPlanVO.class));

		return list;
	}

	protected Map<String, Map<String, UFDouble>> getMonthlyAdjlMap()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");
		sql.append("select Project pk_project ")
				// ��Ŀ����
				.append(", Pro_Phases pk_periodization")
				// ��Ŀ��������
				.append(",Dyear_Change||'-'||to_char(Dmoon_Change,'FM00')datatypename ")
				.append(",Financing_Type pk_fintype")
				// �·�
				.append(",sum(nvl(Fin_Plan,0)) mny")
				.append(" from tgrz_MoonFinancingPlan  ")
				.append("  where dr = 0 and tgrz_MoonFinancingPlan.Approvestatus = 1 and Dyear_Change<='"
						+ year + "' ");
		if (queryValueMap.get("pk_project") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("Project",
							queryValueMap.get("pk_project").split(",")));
		}
		sql.append(" group by Project,Pro_Phases,Dyear_Change,to_char(Dmoon_Change,'FM00'),Financing_Type ");
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

	protected FinMonthlyPlanVO getPlanVO(FinMonthlyPlanVO vo)
			throws BusinessException {
		String typename = typeMap.get(vo.getPk_fintype()).get("name");
		HashMap<String, Object> standard = (HashMap<String, Object>) standardMap
				.get(vo.getPk_fintype());
		if ("��������".equals(typename)) {// ����������
			vo = getExploitPlanData(vo);
		} else if ("ǰ������".equals(typename)) {// ǰ������
			vo = getFrontEndPlanData(vo);

		} else if ("�ղ�������".equals(typename)) {// ��������
			vo = getMAndAPlanData(vo);

		}

		return vo;
	}

	/**
	 * �ղ����¶ȼƻ����
	 * 
	 * ��֤��ȫ����Ȩ�Լ�֧��40%���ϣ���Լ���Ĺ�Ȩ�Լ�֧��ʱ��ǰ3����Ȼ�շŶ�Ӧ���Ĵ���
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	protected FinMonthlyPlanVO getMAndAPlanData(FinMonthlyPlanVO vo)
			throws BusinessException {
		if (vo.getThreecardsdate() != null
				&& !"".equals(vo.getThreecardsdate())
				& vo.getPaymentdate() != null
				&& !"".equals(vo.getPaymentdate())) {
			HashMap<String, Object> standard = (HashMap<String, Object>) standardMap
					.get(vo.getPk_fintype());
			UFDouble ratio = (UFDouble) ((StandardBVO[]) standard
					.get("cycledata"))[0].getAttributeValue(StandardBVO.RATIO1);

			String year = queryValueMap.get("year");
			UFDouble fin_amount = vo.getFin_amount();

			String pk_project = vo.getPk_project();
			String pk_fintype = vo.getPk_fintype();

			String totalmnySQL = "select  sum(b.paymny) totalmny from tgrz_projectdata_b b  where dr = 0 and b.pk_projectdata = '"
					+ pk_project + "'";
			Object value = getBaseDAO().executeQuery(totalmnySQL,
					new ColumnProcessor());
			UFDouble totalmny = value == null ? UFDouble.ZERO_DBL
					: new UFDouble(value.toString());
			if (UFDouble.ZERO_DBL.equals(totalmny)) {
				return vo;
			}

			StringBuffer sql = new StringBuffer();
			sql.append(" select  days ,")
					.append(fin_amount
							+ "* (case when rn = 1 then (ratio - lag(v.ratio,1,0) over(order by days)) -("
							+ ratio.toString()
							+ "/100) else  (ratio - lag(v.ratio,1,0) over(order by days)) end) mny ")
					.append(" from ( ")
					.append(" select to_char((to_date(paydate, 'yyyy-mm-dd hh24:mi:ss')++ to_char(case when v2.days>0 then v2.days - 1 when v2.days<0 then v2.days+1 else v2.days end )),'yyyy-mm-dd') days,row_number() over (order by paydate) rn,v1.ratio ")
					.append("from (select b.pk_projectdata,paydate,trunc(sum(paymny) over(order by paydate)/ "
							+ totalmny.toString() + ",2)  ratio ")
					.append(" from tgrz_projectdata_b b where dr = 0  and b.pk_projectdata = '"
							+ pk_project + "'  ")
					.append(")v1 ,")
					.append("( select days  from tgrz_standard_b b ")
					.append(" where b.pk_standard = (select s.pk_standard from tgrz_standard s where s.periodyear = '"
							+ year
							+ "' and s.pk_fintype = '"
							+ pk_fintype
							+ "' and dr = 0 ) and dr = 0 ").append(")v2")
					.append(" where '" + vo.getPaymentdate() + "'<=v1.paydate")
					.append(" )v");

			List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
					.executeQuery(sql.toString(), new MapListProcessor());
			if (list != null && list.size() > 0) {
				UFDate initdate = UFDate.getDate(year + "-01-01");
				UFDouble initMny = UFDouble.ZERO_DBL;
				UFDouble amout = UFDouble.ZERO_DBL;
				for (Map<String, Object> map : list) {
					UFDate day = new UFDate((String) map.get("days")).asBegin();
					UFDouble mny = map.get("mny") == null ? UFDouble.ZERO_DBL
							: new UFDouble(String.valueOf(map.get("mny")));
					if (initdate.after(day)) {
						initMny = initMny.add(mny);
					}
					if (vo.getMonthtype().equals(
							day.getYear() + "-" + day.getStrMonth())) {
						amout = UFDoubleUtils.add(amout, mny);
					}
				}
				if (vo.getMonthtype().equals(year + "-01")) {
					vo.setInitplan_amount(initMny);
				}
				vo.setLoanmonplan_amount(amout.equals(UFDouble.ZERO_DBL) ? null
						: amout);

			}

		}
		return vo;
	}

	/**
	 * ǰ������
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	protected FinMonthlyPlanVO getFrontEndPlanData(FinMonthlyPlanVO vo)
			throws BusinessException {
		if (vo.getFourcardsdate() != null && !"".equals(vo.getFourcardsdate())
				&& vo.getLandpaydate() != null
				&& !"".equals(vo.getLandpaydate())) {
			// =============================ҵ������������� ���Ϊ����֤��ȫ�����صؼ����ʱ���ִ��
			// ʱ��ȡ��֤��ȫ/�ؼ����ʱ��������ڿ�ʼ���š�============================================
			// String year = queryValueMap.get("year");
			// UFDate initdate = UFDate.getDate(year + "-01-01");
			// UFDouble initMny = UFDouble.ZERO_DBL;
			// String pk_fintype = vo.getPk_fintype();
			// StringBuffer sql = new StringBuffer();
			// sql.append(
			// " select to_char((to_date('"
			// + new UFDate(vo.getLandpaydate()).asBegin()
			// .toStdString()
			// +
			// "', 'yyyy-mm-dd')+ to_char(case when b.days>0 then b.days - 1 when b.days<0 then b.days+1 else b.days end )),'yyyy-mm-dd') days ")
			// .append("  from tgrz_standard_b b")
			// .append(" where b.pk_standard = (select s.pk_standard from tgrz_standard s where s.periodyear = '"
			// + year
			// + "' and s.pk_fintype = '"
			// + pk_fintype
			// +
			// "' and dr = 0 ) and dr = 0 and b.def1 = (select (case when count(1)>1 then '2' else '1' end)landtype  from tgrz_projectdata_b where dr = 0 and pk_projectdata='"
			// + vo.getPk_project() + "')");
			// String days = (String) getBaseDAO().executeQuery(sql.toString(),
			// new ColumnProcessor());
			// if (days != null && !"".equals(days)) {
			// UFDate day = new UFDate(days);
			// UFDouble mny = vo.getFin_amount();
			// if (initdate.after(day)) {
			// initMny = initMny.add(mny);
			// }
			//
			// if (vo.getMonthtype().equals(
			// day.getYear() + "-" + day.getStrMonth())) {
			// vo.setLoanmonplan_amount(mny);
			// }
			//
			// }
			// if (vo.getMonthtype().equals(year + "-01")) {
			// vo.setInitplan_amount(initMny);
			// }
			// =============================ҵ������������� ���Ϊ����֤��ȫ�����صؼ����ʱ���ִ��
			// ʱ��ȡ��֤��ȫ/�ؼ����ʱ��������ڿ�ʼ���š�============================================
			String date = null;
			if (new UFDate(vo.getFourcardsdate()).after(new UFDate(vo
					.getLandpaydate()))) {
				date = new UFDate(vo.getFourcardsdate()).asBegin()
						.toStdString();
			} else {
				date = new UFDate(vo.getLandpaydate()).asBegin().toStdString();
			}

			String year = queryValueMap.get("year");
			String pk_fintype = vo.getPk_fintype();
			UFDouble fin_amount = vo.getFin_amount();
			StringBuffer sql = new StringBuffer();
			sql.append(
					" select days, "
							+ fin_amount
							+ "*(ratio - lag(v.ratio,1,0) over(order by days)) mny from ( ")
					.append("select to_char((to_date('"
							+ date
							+ "', 'yyyy-mm-dd') + to_char(b.days-1)),'yyyy-mm-dd') days ")
					.append(",ratio2/100 ratio ")
					.append("  from tgrz_standard_b b")
					.append(" where b.pk_standard = (select s.pk_standard from tgrz_standard s where s.periodyear = '"
							+ year
							+ "' and s.pk_fintype = '"
							+ pk_fintype
							+ "' and dr = 0 ) and dr = 0) v");
			List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
					.executeQuery(sql.toString(), new MapListProcessor());
			if (list != null && list.size() > 0) {
				UFDate initdate = UFDate.getDate(year + "-01-01");
				UFDouble initMny = UFDouble.ZERO_DBL;
				UFDouble amout = UFDouble.ZERO_DBL;
				for (Map<String, Object> map : list) {
					UFDate day = new UFDate((String) map.get("days")).asBegin();
					UFDouble mny = map.get("mny") == null ? UFDouble.ZERO_DBL
							: new UFDouble(String.valueOf(map.get("mny")));
					if (initdate.after(day)) {
						initMny = initMny.add(mny);
					}
					if (vo.getMonthtype().equals(
							day.getYear() + "-" + day.getStrMonth())) {
						amout = UFDoubleUtils.add(amout, mny);
					}
				}
				if (vo.getMonthtype().equals(year + "-01")) {
					vo.setInitplan_amount(initMny);
				}
				vo.setLoanmonplan_amount(amout.equals(UFDouble.ZERO_DBL) ? null
						: amout);
			}

		}
		return vo;
	}

	/**
	 * ���������¶ȼƻ���� ��֤��ȫ��ִ�мƻ�����
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
	protected FinMonthlyPlanVO getExploitPlanData(FinMonthlyPlanVO vo)
			throws BusinessException {
		if (vo.getFourcardsdate() != null && !"".equals(vo.getFourcardsdate())) {
			String year = queryValueMap.get("year");
			String pk_fintype = vo.getPk_fintype();
			UFDouble fin_amount = vo.getFin_amount();
			StringBuffer sql = new StringBuffer();
			sql.append(
					" select days, "
							+ fin_amount
							+ "*(ratio - lag(v.ratio,1,0) over(order by days)) mny from ( ")
					.append("select to_char((to_date('"
							+ new UFDate(vo.getFourcardsdate()).asBegin()
									.toStdString()
							+ "', 'yyyy-mm-dd') + to_char(b.days-1)),'yyyy-mm-dd') days ")
					.append(",ratio2/100 ratio ")
					.append("  from tgrz_standard_b b")
					.append(" where b.pk_standard = (select s.pk_standard from tgrz_standard s where s.periodyear = '"
							+ year
							+ "' and s.pk_fintype = '"
							+ pk_fintype
							+ "' and dr = 0 ) and dr = 0) v");
			List<Map<String, Object>> list = (List<Map<String, Object>>) getBaseDAO()
					.executeQuery(sql.toString(), new MapListProcessor());
			if (list != null && list.size() > 0) {
				UFDate initdate = UFDate.getDate(year + "-01-01");
				UFDouble initMny = UFDouble.ZERO_DBL;
				UFDouble amout = UFDouble.ZERO_DBL;
				for (Map<String, Object> map : list) {
					UFDate day = new UFDate((String) map.get("days")).asBegin();
					UFDouble mny = map.get("mny") == null ? UFDouble.ZERO_DBL
							: new UFDouble(String.valueOf(map.get("mny")));
					if (initdate.after(day)) {
						initMny = initMny.add(mny);
					}
					if (vo.getMonthtype().equals(
							day.getYear() + "-" + day.getStrMonth())) {
						amout = UFDoubleUtils.add(amout, mny);
					}
				}
				if (vo.getMonthtype().equals(year + "-01")) {
					vo.setInitplan_amount(initMny);
				}
				vo.setLoanmonplan_amount(amout.equals(UFDouble.ZERO_DBL) ? null
						: amout);
			}

		}
		return vo;
	}

	/**
	 * ����ڼ�ʵ�����
	 * 
	 * @return
	 * @throws BusinessException
	 */
	protected Map<String, Map<String, UFDouble>> getMonthlyActualMap()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");
		sql.append("select pro.pk_projectdata pk_project ")
				// ��Ŀ����
				.append(",prostages.pk_projectdata_c pk_periodization")
				// ��Ŀ��������
				.append(",to_char(to_date(contexec.busidate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm') datatypename ")
				.append(",cont.pk_rzlx pk_fintype")
				// �·�
				.append(",sum(nvl(contexec.payamount,0)) mny")
				// ��ȷſ���(�¶�ʵ�ʷſ�)
				// .append(",sum(nvl(contexec.payamount,0)) over(order by to_char(to_date(contexec.busidate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm')) as montotalmny")
				// ��ȷſ���(�¶��ۼ�ʵ�ʷſ�)
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

	protected FinMonthlyPlanVO setMonthlyPlanDate(FinMonthlyPlanVO vo)
			throws BusinessException {
		String typename = typeMap.get(vo.getPk_fintype()).get("name");
		HashMap<String, Object> standard = (HashMap<String, Object>) standardMap
				.get(vo.getPk_fintype());
		String year = queryValueMap.get("year");
		if (standard == null || standard.size() == 0) {
			throw new BusinessException(year + " ���," + typename + " ���ʱ�׼δ����");
		}

		if ("��������".equals(typename)) {// ����������
			String data = getCardsdate(
					(List<String>) standard.get("dataColumn"),
					vo.getPk_project(), vo.getPk_periodization());
			vo.setFourcardsdate(data);

		} else if ("ǰ������".equals(typename)) {// ǰ������
			String data = getCardsdate(
					(List<String>) standard.get("dataColumn"),
					vo.getPk_project(), vo.getPk_periodization());
			vo.setFourcardsdate(data);
			String landdata = getLanddate(vo.getPk_project());
			vo.setLandpaydate(landdata);

		} else if ("�ղ�������".equals(typename)) {// ��������
			String data = getCardsdate(
					(List<String>) standard.get("dataColumn"),
					vo.getPk_project(), vo.getPk_periodization());
			vo.setThreecardsdate(data);
			String data2 = getCounterdate(
					(StandardBVO[]) standard.get("cycledata"),
					vo.getPk_project());

			vo.setPaymentdate(data2);
		}

		return vo;
	}

	/**
	 * ��öԼ�֧��ʱ��
	 * 
	 * @param list
	 * @param standardBVOs
	 * @param pk_project
	 * @param pk_periodization
	 * @return
	 * @throws BusinessException
	 */
	protected String getCounterdate(StandardBVO[] standardBVOs,
			String pk_project) throws BusinessException {
		UFDouble ratio = (UFDouble) standardBVOs[0]
				.getAttributeValue(StandardBVO.RATIO1);
		StringBuffer sql = new StringBuffer();
		sql.append(
				" select min(paydate) from ( select b.paydate, sum(paymny) over(order by paydate) as paymny "
						+ "  from tgrz_projectdata_b b where dr  = 0 and   pk_projectdata ='"
						+ pk_project + "' order by paydate) where paymny>=")
				.append("(select sum(b.paymny)*"
						+ ratio
						+ "/100  from tgrz_projectdata_b b where dr = 0 and b.pk_projectdata = '"
						+ pk_project + "' )");
		String date = (String) getBaseDAO().executeQuery(sql.toString(),
				new ColumnProcessor());
		return date;
	}

	/**
	 * �������֧�����ʱ��
	 * 
	 * @param pk_project
	 * @return
	 * @throws BusinessException
	 */
	protected String getLanddate(String pk_project) throws BusinessException {
		String sql = "select max(tgrz_projectdata_b.paydate)  from tgrz_projectdata_b where dr = 0 and tgrz_projectdata_b.pk_projectdata = '"
				+ pk_project + "'";
		String date = (String) getBaseDAO().executeQuery(sql.toString(),
				new ColumnProcessor());
		return date;
	}

	/**
	 * �����֤/��֤��ȫʱ��
	 * 
	 * @param list
	 * @param pk_periodization
	 * @param pk_project
	 * @return
	 * @throws BusinessException
	 */
	protected String getCardsdate(List<String> list, String pk_project,
			String pk_periodization) throws BusinessException {
		String columnSQL = "";
		for (int i$ = 0; i$ < list.size(); i$++) {
			columnSQL += list.get(i$);
			if (i$ < (list.size() - 1)) {
				columnSQL += ",";
			}
		}

		String table = pk_periodization != null ? "tgrz_projectdata_c"
				: "tgrz_projectdata";
		String key = pk_periodization != null ? "pk_projectdata_c"
				: "pk_projectdata";
		String pk = pk_periodization != null ? pk_periodization : pk_project;
		StringBuffer sql = new StringBuffer();

		sql.append(" select max(nvl(datadate,'9999-12-31')) datadate ")
				.append(" from ((select f.pk_projectdata, " + columnSQL
						+ " from " + table + " f  where nvl(dr, 0) = 0 and f."
						+ key + " = '" + pk + "')  T ")
				.append(" UNPIVOT(datadate FOR " + table + " IN( " + columnSQL
						+ ")))").append(" group by pk_projectdata ");
		String date = (String) getBaseDAO().executeQuery(sql.toString(),
				new ColumnProcessor());

		return "9999-12-31".equals(date) ? null : date;
	}

	/**
	 * ��������������Ϣ
	 * 
	 * @throws BusinessException
	 */
	protected void setTypeMap() throws BusinessException {
		typeMap.clear();
		String sql = "select tgrz_fintype.pk_fintype,tgrz_fintype.code,tgrz_fintype.name from tgrz_fintype where dr = 0";
		List<Map<String, String>> list = (List<Map<String, String>>) getBaseDAO()
				.executeQuery(sql, new MapListProcessor());
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				if (!typeMap.containsKey(map.get("pk_fintype"))) {
					typeMap.put(map.get("pk_fintype"),
							new HashMap<String, String>());
				}
				typeMap.get(map.get("pk_fintype")).put("code", map.get("code"));
				typeMap.get(map.get("pk_fintype")).put("name", map.get("name"));
			}

		}

	}

	/**
	 * ���õ�ǰ������ʱ�׼
	 * 
	 * @throws BusinessException
	 */
	protected void setStandardMap() throws BusinessException {
		Collection<AggStandardVO> coll = getQueryService().queryBillOfVOByCond(
				AggStandardVO.class,
				"tgrz_standard.dr=0 and tgrz_standard.enablestate = 2 and periodyear = '"
						+ queryValueMap.get("year") + "'", false);
		standardMap.clear();
		if (coll != null && coll.size() > 0) {
			for (AggStandardVO aggVO : coll) {
				StandardVO headVO = aggVO.getParentVO();
				StandardBVO[] bodyVOos = (StandardBVO[]) aggVO
						.getChildren(StandardBVO.class);
				String type = (String) headVO
						.getAttributeValue(StandardVO.PK_FINTYPE);
				List<String> datelist = new ArrayList<String>();
				if ("Y".equals(aggVO.getParentVO().getAttributeValue(
						StandardVO.DEF1))) {
					datelist.add("nc_datadate4");// ��������ʹ��֤
				}
				if ("Y".equals(aggVO.getParentVO().getAttributeValue(
						StandardVO.DEF2))) {
					datelist.add("nc_datadate5");// �õع滮���֤
				}
				if ("Y".equals(aggVO.getParentVO().getAttributeValue(
						StandardVO.DEF3))) {
					datelist.add("nc_datadate6");// ���蹤�̹滮���֤
				}
				if ("Y".equals(aggVO.getParentVO().getAttributeValue(
						StandardVO.DEF4))) {
					datelist.add("nc_datadate7");// ʩ�����֤
				}

				if ("��������".equals(typeMap.get(type).get("name"))) {// ����������
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dataColumn", datelist);
					map.put("cycledata", bodyVOos);

					standardMap.put(type, map);

				} else if ("ǰ������".equals(typeMap.get(type).get("name"))) {// ǰ������
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("cycledata", bodyVOos);
					map.put("dataColumn", datelist);
					standardMap.put(type, map);

				} else if ("�ղ�������".equals(typeMap.get(type).get("name"))) {// ��������
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("dataColumn", datelist);
					map.put("cycledata", bodyVOos);
					standardMap.put(type, map);

				}

			}
		}

	}

	/**
	 * ��ȡ�¶����ʼƻ�VO
	 * 
	 * @return
	 * @throws BusinessException
	 */
	protected FinMonthlyPlanVO[] getFinMonthlyPlanVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("year");
		sql.append("select  *  from (select pro.pk_projectdata pk_project ")
				// ��Ŀ����
				.append(",pro.projectcode")
				// ��Ŀ����
				.append(",pro.projectname ")
				// ��Ŀ����
				.append(",prostages.pk_projectdata_c pk_periodization")
				// ��Ŀ��������
				.append(",prostages.periodizationname")
				// ��Ŀ����
				.append(",pro.pk_fintype pk_fintype")
				// ��������
				// .append(",nvl2(finplan.nfinancing_money,pro.nmny,0)  fin_amount")
				.append(",decode(finplan.nfinancing_money,NULL,pro.nmny,finplan.nfinancing_money ) fin_amount")
				// ���ʽ��
				.append(",nvl(finplan.nfinancing_plan,0) finyearplan_amount")
				// ������ʼƻ����
				// .append(",decode(sum(nvl(moonfinplan.fin_plan,0)),0, nvl(finplan.nfinancing_plan, 0),sum(nvl(moonfinplan.fin_plan,0))) finyearplanadj_amount ")
				// ������ʼƻ�����������
				.append(",sum(nvl(contexec.payamount,0)) loanyear_amount")
				// modify swh 20200224 start
				// .append(",(select sum(nvl(contexec.payamount, 0)) from cdm_contract_exec contexec  ")
				// .append("where contexec.pk_contract = cont.pk_contract  and contexec.dr = 0 and contexec.summary = 'FBJ' ")
				// .append("and contexec.busidate like '"+ year +
				// "%') loanyear_amount ")
				// ��ȷſ���
				.append(", sum(nvl(contexectotal.payamount,0)) loanyeartotal_amount")
				// �ۼƷſ���

				.append(",(decode(finplan.nfinancing_money,NULL, pro.nmny,finplan.nfinancing_money) -sum(nvl(contexectotal.payamount,0))) notloan_amount")
				// δ�ſ���
				.append(", replace(LISTAGG(substr(contexectotal.busidate, 1, 4) || '��' || substr(contexectotal.busidate, 6, 2) || '��' ||"
						+ "  substr(contexectotal.busidate, 9, 2) || '��' "
						+ " || '�ſ�' || trim(to_char(nvl(contexectotal.payamount, 0) /  10000,'fm99999990.009999'))|| '��Ԫ')"
						+ " within group (order by  pro.pk_projectdata,prostages.pk_projectdata_c,pro.pk_fintype, contexectotal.busidate),  '�����շſ�0.00��Ԫ',  '')"
						+ " loandate ")
				// �ſ�ʱ��
				.append(" from (" + getFinMonthlyPlanMidSQL() + ") pro ")
				.append(" left join tgrz_FinancingPlan finplan on pro.pk_projectdata = finplan.Pk_Project and finplan.dr = 0 and finplan.dfinancing_year= '"
						+ year
						+ "' and finplan.Financing_Type = pro.pk_fintype")
				.append(" left join tgrz_projectdata_c prostages on prostages.pk_projectdata_c = finplan.project_phases and prostages.dr = 0 ")
				.append(" left join cdm_contract cont on  cont.pk_project= pro.pk_projectdata and cont.pk_rzlx =pro.pk_fintype and cont.dr =0 "
						+ "  and contstatus <>'OVERED'"
						+ "  and cont.vdef3= (case when finplan.project_phases is null then  cont.vdef3 else finplan.project_phases  end )")

				.append(" left join cdm_contract_exec contexectotal on contexectotal.pk_contract=cont.pk_contract and contexectotal.dr =0  and  contexectotal.summary='FBJ'   ")
				// and contexectotal.busidate like '"+ year + "%'
				.append(" left join cdm_contract_exec contexec on contexec.pk_contract=cont.pk_contract and contexec.dr =0  and  contexec.summary='FBJ' and contexec.busidate like '"
						+ year
						+ "%' and contexectotal.pk_contract_exec = contexec.pk_contract_exec")

				.append(" left join (		 ")
				.append(" select sum(nvl(tgrz_MoonFinancingPlan.fin_plan,0)) fin_plan, ")
				.append(" tgrz_MoonFinancingPlan.financing_type financing_type, ")
				.append(" tgrz_MoonFinancingPlan.project ")
				.append(" from tgrz_MoonFinancingPlan ")
				.append(" where tgrz_MoonFinancingPlan.dr = 0 ")
				.append(" and tgrz_MoonFinancingPlan.dyear_Change = '" + year
						+ "'  ").append(" group by  ")
				.append(" tgrz_MoonFinancingPlan.financing_type, ")
				.append(" tgrz_MoonFinancingPlan.project) moonfinplan	 ")
				.append(" on pro.pk_projectdata = moonfinplan.project  ")
				.append(" and moonfinplan.financing_type =  pro.pk_fintype ")
				.append("  where 1=1  ");

		sql.append(
				" group by pro.pk_projectdata,pro.projectcode,pro.projectname, pro.nmny,pro.pk_fintype")
				.append(",prostages.pk_projectdata_c,prostages.periodizationname ")
				// cont.pk_contract,
				.append(",prostages.pk_projectdata_c,prostages.periodizationname")
				.append(",finplan.nfinancing_money,finplan.nfinancing_plan ")
				.append(" )  where   fin_amount<>'0' or finyearplan_amount<>'0' or  loanyear_amount<>'0'or loanyeartotal_amount<>'0' ");

		List<FinMonthlyPlanVO> list = (List<FinMonthlyPlanVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(FinMonthlyPlanVO.class));

		return list != null && list.size() > 0 ? list
				.toArray(new FinMonthlyPlanVO[0]) : null;
	}

	protected String getFinMonthlyPlanMidSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append("select v1.pk_projectdata")
				.append(",v1.code                 projectcode")
				.append(",v1.name                 projectname")
				.append(",tgrz_fintype.pk_fintype")
				.append(",tgrz_fintype.code       fintypecode")
				.append(",tgrz_fintype.name       finypename")
				.append(",case when tgrz_fintype.name = 'ǰ������' then to_number(decode(tgrz_projectdata_f.def1,'~','0',null,'0',tgrz_projectdata_f.def1))  "
						+ " when tgrz_fintype.name = '�ղ�������' then to_number(decode(tgrz_projectdata_f.def2,'~','0',null,'0',tgrz_projectdata_f.def2))"
						+ " else  to_number(decode(tgrz_projectdata_f.def3,'~','0',null,'0',tgrz_projectdata_f.def3)) end nmny")
				.append("  from ( select pk_projectdata, code, name, '��������' fintype from tgrz_projectdata where dr =0");
		if (queryValueMap.get("pk_project") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("pk_projectdata", queryValueMap
							.get("pk_project").split(",")));
		}
		sql.append(" union all ")
				.append(" select pk_projectdata, code, name,")
				.append(" case when projecttype = '���Ĺ�' then 'ǰ������' when projecttype = '���Ĺ���Ŀ' then 'ǰ������' when projecttype = '��Ȩ�չ�' then  '�ղ�������' when projecttype = '�ղ�����Ŀ' then  '�ղ�������' else null end fintype ")
				.append("  from tgrz_projectdata where projecttype in ('���Ĺ�', '��Ȩ�չ�','���Ĺ���Ŀ', '�ղ�������','�ղ�����Ŀ') and dr = 0  ");
		if (queryValueMap.get("pk_project") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn("pk_projectdata", queryValueMap
							.get("pk_project").split(",")));
		}
		sql.append(" ) v1")
				.append(" inner join tgrz_fintype on tgrz_fintype.name = v1.fintype  and tgrz_fintype.dr = 0 ")
				.append(" left join tgrz_projectdata_f  on tgrz_projectdata_f.dr = 0 and tgrz_projectdata_f.pk_projectdata = v1.pk_projectdata");

		return sql.toString();

	}

	/**
	 * ��ʼ����ѯ������Ϣ
	 * 
	 * @param condVOs
	 */
	protected void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		for (ConditionVO condVO : condVOs) {
			if (condVO.getValue() != null && !"".equals(condVO.getValue())) {
				queryValueMap.put(condVO.getFieldCode(), condVO.getValue()
						.replace("(", "").replace(")", "").replace("'", ""));
			}
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}

	protected String getMonByYearCursorTatable() throws BusinessException {
		String year = queryValueMap.get("year");
		String tablename = SQLUtil.createTempTable("temp_tgrz_periodyear",
				"datatype varchar(10),datatypename varchar(20)", null);
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into " + tablename + " (");
		for (int i$ = 1; i$ <= 12; i$++) {
			String month = String.format("%02d", i$);
			sql.append("select '" + year + "-" + month + "' datatype, '"
					+ month + "��' datatypename from dual ");
			if (i$ < 12) {
				sql.append(" union all ");
			}
		}
		sql.append(")");
		getBaseDAO().executeUpdate(sql.toString());
		return tablename;

	}

	public IMDPersistenceQueryService getQueryService() {
		if (queryService == null) {
			queryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return queryService;
	}

}
