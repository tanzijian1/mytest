package nc.bs.tg.rz.report;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.projectdata.ProjectDataBVO;
import nc.vo.tg.rz.report.FinancingProVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;
/*
 * 融资进度表
 */
public class FinancingProUtils extends FinancingProParentUtils {
	static FinancingProUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql
	Map<String,ProjectDataBVO> project=new HashMap<String,ProjectDataBVO>();
	public static FinancingProUtils getUtils() {
		if (utils == null) {
			utils = new FinancingProUtils();
		}
		return utils;
	}

	public FinancingProUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinancingProVO()));
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

			List<FinancingProVO> vos = getFinancingProVOs();
			if (vos != null && vos.size() > 0) {
				cmpreportresults = transReportResult(vos);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return generateDateset(cmpreportresults, getKeys());
	}

	/**
	 * 读取
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	private List<FinancingProVO> getFinancingProVOs() throws BusinessException {
		StringBuffer sqlBuff = new StringBuffer();
		//--方案编号
		sqlBuff.append("select distinct tgrz_fisceme.billno schemeno ,tgrz_fisceme.pk_scheme  ")
		//--拟融资类型 
		.append(",tgrz_fintype.name nfintype ")
		//--项目主键
		.append(",tgrz_projectdata.pk_projectdata pk_projectdata ")
		//--项目名称
		.append(",tgrz_projectdata.name proname ")
		//地价付讫金额
		//.append(",tgrz_projectdata_b.paymny paymny_amount ")
		//--地价付讫时间
		.append(",substr((case when tgrz_fintype.name = '开发贷' then '' else  (select max(paydate) ")
		.append("from tgrz_projectdata_b b where b.pk_projectdata = tgrz_projectdata.pk_projectdata) end),1,10) bpaydate  ")
		//--四证齐全时间
		.append(",substr(greatest(tgrz_projectdata.nc_datadate4,tgrz_projectdata.nc_datadate5,tgrz_projectdata.nc_datadate6,tgrz_projectdata.nc_datadate7),1,10) fourcompletetime ")
		//--总建筑面积
		.append(",tgrz_projectdata_p.def2 buildingarea_amount ")
		//--考核融资金额
		.append(",(case when tgrz_fintype.name = '前端融资' then tgrz_projectdata_f.def1 when tgrz_fintype.name = '收并购融资' then tgrz_projectdata_f.def2 ")
		.append("when tgrz_fintype.name = '开发贷款' then tgrz_projectdata_f.def3 else ''end ) exfin_amount ")
		//--融资机构
		.append(",tgrz_organization.name norganization ")
		//--拟融资金额
		.append(",tgrz_fisceme.nmy finmny_amount ")
		//--是否主推
		.append(",(case when tgrz_fisceme.bmain='y' then '是' else '否' end ) ismain ")
		//--备注
		.append(",tgrz_fisceme.big_text_a note ")
		.append("from  tgrz_fisceme  ")
		.append("left  join tgrz_fintype on tgrz_fintype.pk_fintype=tgrz_fisceme.pk_organizationtype and tgrz_fintype.dr=0 ")
		.append("left join tgrz_projectdata on tgrz_projectdata.pk_projectdata=tgrz_fisceme.pk_project and tgrz_projectdata.dr=0 ")
		.append("left join tgrz_projectdata_b on tgrz_projectdata_b.pk_projectdata= tgrz_projectdata.pk_projectdata and tgrz_projectdata_b.dr=0 ")
		.append("left  join tgrz_projectdata_p on tgrz_projectdata_p.pk_projectdata = tgrz_projectdata.pk_projectdata and tgrz_projectdata_p.dr=0 ")
		.append("left  join tgrz_projectdata_f on tgrz_projectdata_f.pk_projectdata = tgrz_projectdata.pk_projectdata and tgrz_projectdata_f.dr=0  ")
		.append("left  join tgrz_organization on  tgrz_fisceme.pk_organization=tgrz_organization.pk_organization and tgrz_organization.dr=0 ")
		.append("where 1=1 and  tgrz_fisceme.dr='0' and tgrz_fisceme.approvestatus='1'  ");//
		if (queryValueMap.get("pk_projectdata") != null) {// 项目名称
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.pk_projectdata",
							queryValueMap.get("pk_projectdata").split(",")));
		}

		if (queryValueMap.get("pk_fintype") != null) {// 融资类型
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_fintype.pk_fintype",
							queryValueMap.get("pk_fintype").split(",")));
		}

		if (queryValueMap.get("pk_organization") != null) {// 融资机构
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.pk_organization",
							queryValueMap.get("pk_organization").split(",")));
		}
		sqlBuff.append(" order by tgrz_fisceme.billno ");
		Collection<FinancingProVO> coll = (Collection<FinancingProVO>) getBaseDAO()
				.executeQuery(sqlBuff.toString(),
						new BeanListProcessor(FinancingProVO.class));
		List<FinancingProVO> listVO =null;
		if (coll.size() > 0) {
			// 针对收并购融资的地价付讫时间进行逻辑处理
			HashSet<String> set = getFinProjectdata(coll);
			List<ProjectDataBVO> vos = null;
			if (set != null && set.size() > 0) {
				vos = getProjectDate(set);
			}
			for (String s : set) {
				// 地价付讫时间
				List<String> paydate = getPaydate(vos, s);
				// 地价付讫金额
				List<Double> paymny = getPaymny(vos, s);
				// 地价付讫总额
				List<Double> summny = getSummny(vos, s);
				Double pay = 0.0;
				Double beforPercentage = 0.0;
				Double afterPercentage = 0.0;
				Double rate =getTatio1();
				int num = 0;
				// 数据处理

				if (paymny.size() > 0 && paymny.get(0) != null) {
					for (int i = 0; i < paymny.size(); i++) {
						beforPercentage = pay / (summny.get(0));
						pay = pay + paymny.get(i);
						afterPercentage = pay / (summny.get(0));
						if (afterPercentage > rate.doubleValue()/100) {
							if (Math.abs(afterPercentage - rate.doubleValue()/100) < Math
									.abs(beforPercentage - rate.doubleValue()/100)) {
								num = i;
							} else {
								num = i - 1;
							}

							break;
						}
					}
				}
				for (FinancingProVO financingProVO : coll) {
					if (s.equals(financingProVO.getPk_projectdata())
							&& paydate.size() > 0) {
						financingProVO.setBpaydate(paydate.get(num));
					}
				}
			}

			
			HashSet<String> setScheme = getFinscheme(coll);
			 listVO=	filterFinDate(setScheme,coll);

		}

		return listVO;
		/*return coll != null && coll.size() > 0 ? coll
				.toArray(new FinancingProVO[0]) : null;*/
	}
	
	
	/*
	 * 获取项目资料，融资类型为收并购的低价_对价支付表体VO
	 */
	public List<ProjectDataBVO> getProjectDate(HashSet<String> set) throws BusinessException{
	String last=	changeData(set);
	StringBuffer sql= new StringBuffer();
	
	sql.append("select tgrz_projectdata_b.pk_projectdata, tgrz_projectdata_b.paydate,tgrz_projectdata_b.paymny from tgrz_projectdata_b where tgrz_projectdata_b.pk_projectdata in ("+last+")" );
	List<ProjectDataBVO> vos =	(List<ProjectDataBVO>) getBaseDAO().executeQuery(sql.toString(),new  BeanListProcessor(ProjectDataBVO.class));
	
	return vos;
		
	}
	

	
	/*
	 * 获取收并购当前查询年份的地价/股价支付占比
	 */
	public Double getTatio1(){
		StringBuffer sql = new StringBuffer();
		sql.append("select   ratio1  from  tgrz_standard_b ")
		.append("left join tgrz_standard on tgrz_standard.pk_standard=tgrz_standard_b.pk_standard ")
		.append("left join tgrz_fintype on tgrz_standard.pk_fintype=tgrz_fintype.pk_fintype	")
		.append("where tgrz_fintype.name='收并购融资' and periodyear='"+queryValueMap.get("year")+"' ");
		Double rate=Double.NaN;
		try {
			Object[] ratio1=	(Object[]) getBaseDAO().executeQuery(sql.toString(),new ArrayProcessor());
		BigDecimal rate1 =	(BigDecimal) ratio1[0];	
		rate =rate1.doubleValue();	
		//rate=new  Double(()ratio1[0]) ;
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return rate;
		
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
			if (condVO.getValue() != null && !"".equals(condVO.getValue())) {
				if (condVO.getDataType() == ConditionVO.DATE) {
					String[] dates = condVO.getValue().split("@@");
					String dateBegin = null;
					//处理只填结束日期的报错 add by tjl 2020-07-14
					if(dates[0].contains("#,")){
						dateBegin = dates[0].replaceAll("#,", "").trim();
					}
					if(StringUtils.isNotBlank(dateBegin)){
						queryValueMap.put(condVO.getFieldCode() + "_begin",
								new UFDate(dateBegin).asBegin().toString());
						queryValueMap.put(condVO.getFieldCode() + "_end",
								new UFDate(dates[dates.length - 1]).asEnd()
								.toString());
					}else{
						
						queryValueMap.put(condVO.getFieldCode() + "_begin",
								new UFDate(dates[0]).asBegin().toString());
						queryValueMap.put(condVO.getFieldCode() + "_end",
								new UFDate(dates[dates.length - 1]).asEnd()
								.toString());
					}
					//end
				} else {
					queryValueMap
							.put(condVO.getFieldCode(),
									condVO.getValue().replace("(", "")
											.replace(")", "").replace("'", ""));
				}
			}
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}
}
