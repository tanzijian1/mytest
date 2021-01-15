package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.itf.cmp.report.iufo.SmartProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.projectdata.ProjectDataBVO;
import nc.vo.tg.rz.report.FinMonthlyPlanTotalVO;
import nc.vo.tg.rz.report.FinancingProVO;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;

public abstract class ReportUtils {
	String[] keys = null;
	BaseDAO baseDAO = null;

	protected String[] getKeys() {
		return keys;
	}

	protected void setKeys(String[] keys) {
		this.keys = keys;
	}

	/**
	 * 报表加工处理类
	 * 
	 * @param context
	 * @return
	 * @throws BusinessException
	 */
	public abstract DataSet getProcessData(IContext context)
			throws BusinessException;

	/**
	 * 将转换信息生成为DataSet
	 * 
	 * @param cmpreportresults
	 * @param keys
	 * @return
	 * @throws BusinessException
	 */
	protected DataSet generateDateset(CmpReportResultVO[] cmpreportresults,
			String[] keys) throws BusinessException {
		if (cmpreportresults == null) {
			if (cmpreportresults == null || cmpreportresults.length == 0) {
				if (cmpreportresults == null) {
					cmpreportresults = new CmpReportResultVO[] { new CmpReportResultVO(
							getKeys()) };
				}
			}
		}

		DataSet resultDataSet = new DataSet();
		resultDataSet.setMetaData(SmartProcessor.getMetaData(cmpreportresults));
		resultDataSet.setDatas(SmartProcessor.getDatas(cmpreportresults));
		return resultDataSet;
	}

	/**
	 * 转换报表返回参数类型
	 * 
	 * @param vos
	 * @return
	 * @throws Exception
	 */
	protected CmpReportResultVO[] transReportResult(List volist)
			throws Exception {
		List<CmpReportResultVO> resultVOs = new ArrayList<CmpReportResultVO>();
		for (Object vo : volist) {
			CmpReportResultVO resultVO = new CmpReportResultVO(getKeys());
			for (int i$ = 0; i$ < keys.length; i$++) {
				if (!"primarykey".equals(keys[i$])) {
					resultVO.setValue(i$, BeanHelper.getProperty(vo, keys[i$]));
				}
			}
			resultVOs.add(resultVO);
		}
		return resultVOs.toArray(new CmpReportResultVO[0]);
	}

	/**
	 * 
	 * @param startDay
	 *            开始日期
	 * @param endDay
	 *            截止日期
	 * @return
	 * @throws Exception
	 */
	public static int getDaysBetween(UFDate startDate, UFDate endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate.asBegin().toDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate.asEnd().toDate());
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	/**
	 * 获取实际放款合计明细(每月)
	 * @param coll
	 * @return
	 */
	public Map<String,Object> getLoanmonactualtotal_amount(Collection<FinMonthlyPlanTotalVO> coll){
		Map<String,Object> refMap = new LinkedHashMap<String,Object>();
		for (FinMonthlyPlanTotalVO finMonthlyPlanTotalVO : coll) {
			refMap.put(finMonthlyPlanTotalVO.getMonth(),finMonthlyPlanTotalVO.getLoanactualtotal_amount());
		}
		return refMap;
	}
	
	/**
	 * 获取计划放款合计明细(每月)
	 * @param coll
	 * @return
	 */
	public Map<String,Object> getLoanplantotal_amount(Collection<FinMonthlyPlanTotalVO> coll){
		Map<String,Object> refMap = new LinkedHashMap<String,Object>();
		for (FinMonthlyPlanTotalVO finMonthlyPlanTotalVO : coll) {
			refMap.put(finMonthlyPlanTotalVO.getMonth(),finMonthlyPlanTotalVO.getLoanplantotal_amount());
		}
		return refMap;
	}
	
	/**
	 * 累计融资计划放款金额调整
	 * @param coll
	 * @return
	 */
	public Map<String,Object> getFinancingTotal(Collection<FinMonthlyPlanTotalVO> coll){
		Map<String,Object> refMap = new LinkedHashMap<String,Object>();
		for (FinMonthlyPlanTotalVO finMonthlyPlanTotalVO : coll) {
			refMap.put(finMonthlyPlanTotalVO.getMonth(),finMonthlyPlanTotalVO.getLoanmonplantotal_amount());
		}
		return refMap;
	}
	
	/**
	 * 项目资料主键
	 * @param coll
	 * @return
	 */
	public HashSet<String> getFinProjectdata(Collection<FinancingProVO> coll){
		HashSet<String> set = new HashSet<String>();
		for (FinancingProVO financingProVO : coll) {
			if(financingProVO.getPk_projectdata()!=null && "收并购融资".equals(financingProVO.getNfintype()))
				set.add(financingProVO.getPk_projectdata());
		}
		return set;
	}
	
	/**
	 * 融资方案
	 * @param coll
	 * @return
	 */
	public HashSet<String> getFinscheme(Collection<FinancingProVO> coll){
		HashSet<String> set = new HashSet<String>();
		for (FinancingProVO financingProVO : coll) {
				set.add(financingProVO.getPk_scheme());
		}
		return set;
	}
	
	/**
	 * 融资进度地价付讫时间
	 * @param coll
	 * @return
	 */
	public List<String> getPaydate(List<ProjectDataBVO> vos,String s){
		List<String> list = new ArrayList<>();
		for (ProjectDataBVO vo : vos) {
			if(s.equals(vo.getPk_projectdata())){
				list.add(vo.getPaydate().asBegin().toString());
			}
		}
		return list;
	}
	
	/**
	 * 融资进度地价付讫金额
	 * @param coll
	 * @return
	 */
	public List<Double> getPaymny(List<ProjectDataBVO>vos,String s){
		List<Double> list = new ArrayList<>();
		Double paymny = new Double(0);
		for (ProjectDataBVO vo : vos) {
			if(s.equals(vo.getPk_projectdata())){
				if(vo.getPaymny()!=null){
					 paymny = vo.getPaymny().toDouble();
					 list.add(paymny);
						}
				}
			}
		return list;
	}
	
	/**
	 * 融资进度地价付讫总额
	 * @param coll
	 * @return
	 */
	public List<Double> getSummny(List<ProjectDataBVO> vos,String s){
		List<Double> list = new ArrayList<>();
		Double paymny = new Double(0);
		for (ProjectDataBVO vo : vos) {
			if(s.equals(vo.getPk_projectdata())){
				if(vo.getPaymny()!=null){
					 paymny = paymny+vo.getPaymny().toDouble();
				
						}
				}
		}
		list.add(paymny);
		return list;
	}
	
	private static final int SQL_IN_LIST_LIMIT = 200;
	/**
	 * 构造Like子句
	 */
	public static String buildSqlForIn(final String fieldname,
			final String[] fieldvalue) {
		StringBuffer sbSQL = new StringBuffer();
		sbSQL.append("(" + fieldname + " LIKE ( ");
		int len = fieldvalue.length;
		// 循环写入条件
		for (int i = 0; i < len; i++) {
			if (StringUtils.isNotBlank(fieldvalue[i])) {
				sbSQL.append("'%").append(fieldvalue[i]).append("%'");
				if (i == (fieldvalue.length - 1)) {
					continue;
				} else if (i > 0 && (i + 1) % SQL_IN_LIST_LIMIT == 0) {
					// 处理in长度不超过200的问题
					sbSQL.append(" ) OR ").append(fieldname).append(" IN ( ");
				} else {
					if(i<len-1&&StringUtils.isNotBlank(fieldvalue[i+1])){
						sbSQL.append(",");
					}
				}
			}
		}
		sbSQL.append(" )) ");
		return sbSQL.toString();
	}

}
