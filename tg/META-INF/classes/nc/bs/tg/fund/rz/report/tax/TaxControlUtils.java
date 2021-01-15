package nc.bs.tg.fund.rz.report.tax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.tg.fund.rz.report.ReportUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.ui.tb.tree.policy.TacticsMemberTreePolicy;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tgfn.report.tax.TaxControlVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 税控表（税务报表）
 * 
 * @author ASUS
 * 
 */
public class TaxControlUtils extends ReportUtils {
	static TaxControlUtils utils;

	public static TaxControlUtils getUtils()
			throws BusinessException {
		if (utils == null) {
			utils = new TaxControlUtils();
		}
		return utils;
	}

	public TaxControlUtils() throws BusinessException {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new TaxControlVO()));
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
			List<TaxControlVO> list = null;
			list = onMergeData(conditionVOs);
			
			if (list.size() == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			
			cmpreportresults = transReportResult(list);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
		
	}
	
	
	/**
	 * 信息整合
	 * 
	 * @param pk_financeorg
	 * @return
	 * @throws BusinessException
	 */
	protected List<TaxControlVO> onMergeData(ConditionVO[] conditionVOs) throws BusinessException{
		List<Map<String,String>> list = getNextStageOrgList(conditionVOs);
		
		List<TaxControlVO> taxControlVOs = new ArrayList<TaxControlVO>();
		
		if(null!= list && list.size()>0){
			for (Map<String, String> map : list) {
				TaxControlVO taxControlVO = new TaxControlVO();
				taxControlVO.setDef24(map.get("def24"));//城市公司
				taxControlVO.setProject_code(map.get("project_code"));//分期编号
				taxControlVO.setAreanum(map.get("areanum"));//区域编号
				taxControlVO.setDef58(map.get("def58"));//税务机关所在行政区/镇
				taxControlVO.setSuppliername(map.get("suppliername"));//主管税务机关
				taxControlVO.setOrgname(map.get("orgname"));//企业名称(纳税主体)
				taxControlVO.setProject_name(map.get("project_name"));//分期名称
				taxControlVO.setProjecttype(map.get("projecttype"));//项目类型
				taxControlVO.setTaxclass(map.get("taxclass"));//税种-税目
				taxControlVO.setDef52(map.get("def52"));//所属期
				taxControlVO.setBilldate(map.get("billdate"));//缴纳期
				taxControlVO.setDef43amount(tranUFDouble(map.get("def43amount")));//已缴税费
				taxControlVO.setMoneyamount(tranUFDouble(map.get("moneyamount")));//请款税金
				taxControlVO.setProjectproperty(map.get("projectproperty"));
				taxControlVO.setIsmerge(map.get("ismerge"));
				taxControlVOs.add(taxControlVO);
			}
		}
		return taxControlVOs;
	}
	
	/**
	 * String转ufdouble
	 * @param str
	 * @return
	 */
	public UFDouble tranUFDouble(String str){
		if(str!=null&&str.length()>0){
			return new UFDouble(str);
		}
		return UFDouble.ZERO_DBL;
	}
	/**
	 * 查询税务监控表
	 * 	
	 * @param pk_financeorg
	 * @return
	 * @throws BusinessException
	 */
	protected List<Map<String,String>> getNextStageOrgList(ConditionVO[] conditionVOs)
			throws BusinessException {
 		StringBuffer sql = getTaxMonitoringSQL();
 		if (conditionVOs != null && conditionVOs.length > 0) {
			String wheresql = new ConditionVO().getWhereSQL(conditionVOs);
			if (wheresql != null && !"".equals(wheresql)) {
				sql.append(" and "
						+ new ConditionVO().getWhereSQL(conditionVOs));
			}
		}

		List<Map<String,String>> list = (List<Map<String,String>>) getBaseDAO().executeQuery(
				sql.toString(), new MapListProcessor());
		
		return list;
	}
	
	/*
	 * 
	 */
	protected StringBuffer getTaxMonitoringSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append("select "+System.getProperty("line.separator"));//
		sql.append("orgn.name def24,"+System.getProperty("line.separator"));//--城市公司
		sql.append("p.project_code ,"+System.getProperty("line.separator"));//--分期编号
		sql.append("'' areanum,"+System.getProperty("line.separator"));//--区域编号
		sql.append("a.def58 def58,"+System.getProperty("line.separator"));//--税务机关所在行政区/镇
		sql.append("bs.name suppliername ,"+System.getProperty("line.separator"));//--主管税务机关
		sql.append("o.name orgname,"+System.getProperty("line.separator"));//--企业名称(纳税主体)
		sql.append("pn.project_name,"+System.getProperty("line.separator"));//--分期名称
		sql.append("bfa.name projecttype,"+System.getProperty("line.separator"));//--项目类型
		sql.append("bf.name taxclass,"+System.getProperty("line.separator"));//--税种-税目
		sql.append(" (case when nvl(bm.yearmth,'~')!='~' then bm.yearmth when nvl(a.billdate,'~')!='~' then  to_char(ADD_MONTHS(to_date(a.billdate,'yyyy-mm-dd hh24:mi:ss'),-1),'yyyy-mm') else null end) def52,"+System.getProperty("line.separator"));//--所属期
		sql.append("(case when nvl(a.billdate,'~')!='~' then to_char(to_date(a.billdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm')  else null end ) billdate,"+System.getProperty("line.separator"));//--缴纳期
		sql.append("ai.def43  def43amount,"+System.getProperty("line.separator"));//--已缴税费
		sql.append("ai.def72  moneyamount,"+System.getProperty("line.separator"));//--请款税金
		sql.append("'' projectproperty,"+System.getProperty("line.separator"));//--项目性质
		sql.append("'' ismerge "+System.getProperty("line.separator"));//--是否并表
		sql.append("from ap_paybill a"+System.getProperty("line.separator"));//
		sql.append("left join org_orgs o on o.pk_org=a.pk_org"+System.getProperty("line.separator"));//
		sql.append("left join org_orgs orgn on orgn.pk_org=o.pk_fatherorg"+System.getProperty("line.separator"));//
		sql.append("left join ap_payitem ai on ai.pk_paybill=a.pk_paybill"+System.getProperty("line.separator"));//
		sql.append("left join bd_project p on p.pk_project=ai.def36"+System.getProperty("line.separator"));// --分期编号
		sql.append("left join bd_supplier bs on bs.pk_supplier=ai.supplier"+System.getProperty("line.separator"));//--主管税务机关
		sql.append("left join bd_project pn on pn.pk_project=ai.def37 "+System.getProperty("line.separator"));//--分期名称
		sql.append("left join bd_defdoc bf on bf.pk_defdoc=ai.def41"+System.getProperty("line.separator"));//  --税种
		sql.append("left join bd_defdoc bfa on bfa.pk_defdoc=ai.def26"+System.getProperty("line.separator"));//--项目类型
		sql.append(" left join  bd_accperiodmonth bm on bm.pk_accperiodmonth=ai.def52 "+System.getProperty("line.separator"));
		sql.append("where 	a.pk_tradetype='F3-Cxx-012' and nvl(a.dr,0)=0 and nvl(ai.dr,0)=0 and nvl(p.dr,0)=0 and nvl(bs.dr,0)=0 and nvl(ai.def43,'~')!='~'  "+System.getProperty("line.separator"));
		return sql;
	}
	
	/*
	 * 查询所属期
	 */
	protected String getCityCompany(String pk_accperiodmonth) throws DAOException{
		
		String sql = "select bd_accperiodmonth.yearmth from bd_accperiod INNER JOIN bd_accperiodmonth    ON bd_accperiod.pk_accperiod = bd_accperiodmonth.pk_accperiod where bd_accperiodmonth.pk_accperiodmonth = '"+pk_accperiodmonth+"'";
		String yearmth = (String) getBaseDAO().executeQuery(
				sql.toString(), new ColumnProcessor());
		return yearmth;
	}
	
}