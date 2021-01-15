package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinYearLoanVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 年度融资放款表
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:42:09
 */
public class FinYearLoanDataUtils extends ReportUtils {
	static FinYearLoanDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static FinYearLoanDataUtils getUtils() {
		if (utils == null) {
			utils = new FinYearLoanDataUtils();
		}
		return utils;
	}

	public FinYearLoanDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new FinYearLoanVO()));
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

			FinYearLoanVO[] vos = getFinYearLoanListVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	/**
	 * 查询报表sql
	 * add by tjl 2019-08-01
	 * @throws DAOException 
	 */
	private FinYearLoanVO[] getFinYearLoanListVOs() throws BusinessException {
		//测试用
//		if(conditionVOs==null||conditionVOs.length<=0){
//			return null;
//		}
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( ");
		sql.append("select ");
		sql.append("rzxx.年度 as cyear, ");
		sql.append("rzxx.合同主键 as pk_contract, ");
		sql.append("rzxx.合同编号 as contractcode, ");
		sql.append("rzxx.项目ID as pk_project, ");//
		sql.append("to_char(rzxx.财务顾问费率,'fm99999990.000099')||'%' as i_cwgwfl, ");
		sql.append("rzxx.项目编码 as projectcode, ");
		sql.append("rzxx.区域公司名称 as pk_sxqy, ");
		sql.append("rzxx.项目名称 as projectname, ");
		sql.append("rzxx.分期主键 as pk_periodization, ");
		sql.append("rzxx.分期名称 as periodizationname, ");
		sql.append("rzxx.项目公司 as projectcorp, ");
		sql.append("rzxx.借款人 as borrower, ");
		sql.append("rzxx.融资类型 as pk_fintype, ");
		sql.append("rzxx.融资类型编码 as fintypecode, ");
		sql.append("rzxx.融资类型名称 as fintypename, ");
		sql.append("rzxx.融资机构 as pk_organization, ");
		sql.append("rzxx.融资机构编码 as organizationcode, ");
		sql.append("rzxx.融资机构名称 as organizationname, ");
		sql.append("rzxx.融资机构类别 as pk_organizationtype, ");
		sql.append("rzxx.融资机构类别编码 as organizationtypecode, ");
		sql.append("rzxx.融资机构类别名称 as organizationtypename, ");
		sql.append("rzxx.机构分行 as branch, ");
		sql.append("rzxx.机构支行 as subbranch, ");
		sql.append("rzxx.合同起始日期 as begindate, ");
		sql.append("rzxx.合同结束日期 as enddate, ");
		sql.append("nvl(rzxx.融资金额,0) as fin_amount, ");
		sql.append("sum(nvl(rzxx.累计放款金额,0)) as loan_totalamount, ");
		sql.append("sum(nvl(rzxx.上年放款金额,0)) as loan_lastyearamount, ");
		sql.append("sum(nvl(rzxx.当年放款金额,0)) as loan_thisyearamount, ");
		sql.append("nvl(rzxx.融资金额, 0) - sum(nvl(rzxx.累计放款金额, 0)) as loan_awaitamount, ");
		sql.append("to_char(rzxx.合同利率,'fm99999990.000099')||'%' as cont_lv, ");
		sql.append("rzxx.综合利率||'%' as multiple_lv, ");
		//12c适配的行转列函数
//		sql.append("to_char(wmsys.wm_concat(rzxx.t)) as loan_datenote, ");
		//11g适配的行转列函数
		sql.append("to_char(listagg(rzxx.t, ',') within GROUP(order by rzxx.t)) as loan_datenote, ");
		sql.append("sum(nvl(rzxx.\"1月放款金额\",0)) as jan_amount, ");
		sql.append("sum(nvl(rzxx.\"2月放款金额\",0)) as feb_amount, ");
		sql.append("sum(nvl(rzxx.\"3月放款金额\",0)) as mar_amount, ");
		sql.append("sum(nvl(rzxx.\"4月放款金额\",0)) as apr_amount, ");
		sql.append("sum(nvl(rzxx.\"5月放款金额\",0)) as may_amount, ");
		sql.append("sum(nvl(rzxx.\"6月放款金额\",0)) as jun_amount, ");
		sql.append("sum(nvl(rzxx.\"7月放款金额\",0)) as jul_amount, ");
		sql.append("sum(nvl(rzxx.\"8月放款金额\",0)) as aug_amount, ");
		sql.append("sum(nvl(rzxx.\"9月放款金额\",0)) as sep_amount, ");
		sql.append("sum(nvl(rzxx.\"10月放款金额\",0)) as oct_amount, ");
		sql.append("sum(nvl(rzxx.\"11月放款金额\",0)) as nov_amount, ");
		sql.append("sum(nvl(rzxx.\"12月放款金额\",0)) as dec_amount ");
			sql.append("from ( ");
				sql.append("select  ");
				
		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as 年度, ");
		sql.append("cdm_contract.pk_contract as 合同主键, ");
		sql.append("cdm_contract.contractcode as 合同编号, ");
		sql.append(" ROUND((nvl(cdm_contract.i_cwgwfl, 0) * 100),4)+nvl(cdm_contract.pk_htlv,0) as 综合利率, ");
		sql.append(" ROUND((nvl(cdm_contract.i_cwgwfl, 0) * 100),4) as 财务顾问费率, ");
		sql.append("cdm_contract.pk_sxqy as 区域公司主键, ");
		sql.append("org_orgs.name as 区域公司名称, ");
		sql.append("tgrz_projectdata.pk_projectdata as 项目ID, ");
		sql.append("tgrz_projectdata.code as 项目编码, ");
		sql.append("tgrz_projectdata.name as 项目名称, ");
		sql.append("cdm_contract.vdef3 分期主键, ");
		sql.append("tgrz_projectdata_c.periodizationname as 分期名称, ");
		sql.append("tgrz_organization.branch as 机构分行, ");
		sql.append("tgrz_organization.subbranch as 机构支行, ");
		sql.append("org_financeorg.pk_financeorg as 项目公司pk, ");
		sql.append("tgrz_projectdata.projectcorp as 项目公司, ");
		sql.append("org_financeorg.name as 借款人, ");
		sql.append("cdm_contract.pk_rzlx as 融资类型, ");
		sql.append("tgrz_fintype.code as 融资类型编码, ");
		sql.append("tgrz_fintype.name as 融资类型名称, ");
		sql.append("cdm_contract.pk_rzjg as 融资机构, ");
		sql.append("tgrz_organization.code as 融资机构编码, ");
		sql.append("tgrz_organization.name as 融资机构名称, ");
		sql.append("cdm_contract.pk_rzjglb as 融资机构类别, ");
		sql.append("tgrz_OrganizationType.code as 融资机构类别编码, ");
		sql.append("tgrz_OrganizationType.name as 融资机构类别名称, ");
		sql.append("(substr(cdm_contract.begindate,1,11)) as 合同起始日期, ");
		sql.append("(substr(cdm_contract.enddate,1,11)) as 合同结束日期, ");
		sql.append("cdm_contract.contractamount as 融资金额, ");
		sql.append("(case when substr(cdm_contract_exec.busidate, 1, 4) < '"+queryValueMap.get("cyear")+"' then cdm_contract_exec.payamount else 0 end)+ ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,10) >= '"+queryValueMap.get("cyear")+"-01-01' and substr(cdm_contract_exec.busidate,1,10) <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else  0  end)  as 累计放款金额, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,4) = '"+queryValueMap.get("cyear")+"' - '1' then cdm_contract_exec.payamount else 0 end ) as 上年放款金额, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,4) = '"+queryValueMap.get("cyear")+"'  then cdm_contract_exec.payamount else 0 end) as 当年放款金额, ");
//		sql.append("cdm_contract_exec.leftpayamount as 待放款金额, ");
		sql.append("nvl(cdm_contract.pk_htlv,0) as 合同利率, ");
		sql.append("(case when cdm_contract_exec.payamount is null then null else ");
		sql.append("substr(cdm_contract_exec.busidate, 1, 11) || ' 放款' || to_char(cdm_contract_exec.payamount * 0.0001, ");
		sql.append("'fm99999990.009999') || '万元' end) as t, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-01' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-02' then cdm_contract_exec.payamount else 0 end) as \"1月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-02' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-03' then cdm_contract_exec.payamount else 0 end) as \"2月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-03' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-04' then cdm_contract_exec.payamount else 0 end) as \"3月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-04' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-05' then cdm_contract_exec.payamount else 0 end) as \"4月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-05' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-06' then cdm_contract_exec.payamount else 0 end) as \"5月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-06' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-07' then cdm_contract_exec.payamount else 0 end) as \"6月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-07' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-08' then cdm_contract_exec.payamount else 0 end) as \"7月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-08' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-09' then cdm_contract_exec.payamount else 0 end) as \"8月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-09' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-10' then cdm_contract_exec.payamount else 0 end) as \"9月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-10' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-11' then cdm_contract_exec.payamount else 0 end) as \"10月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-11' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-12' then cdm_contract_exec.payamount else 0 end) as \"11月放款金额\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) = "+queryValueMap.get("cyear")+"||'-12'  then cdm_contract_exec.payamount else 0 end) as \"12月放款金额\" ");
		
//		sql.append("from cdm_contract_exec ");
//		sql.append("left join cdm_contract on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("from cdm_contract ");
		sql.append("left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("left join tgrz_projectdata on cdm_contract.pk_project = tgrz_projectdata.pk_projectdata ");
		sql.append("left join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg ");
		sql.append("left join bd_bankdoc on bd_bankdoc.pk_bankdoc = cdm_contract.pk_creditbank ");
		sql.append("left join org_financeorg on org_financeorg.pk_financeorg = cdm_contract.pk_debitorg ");
		sql.append("left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx ");
		sql.append("left join org_orgs on cdm_contract.pk_sxqy = org_orgs.pk_org ");
		sql.append("left join tgrz_OrganizationType on tgrz_OrganizationType.pk_organizationtype = tgrz_organization.pk_organizationtype ");
		sql.append("left join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c = cdm_contract.vdef3 ");
		sql.append("where nvl(cdm_contract.dr,0) = 0 and nvl(cdm_contract_exec.dr,0) = 0 and nvl(tgrz_projectdata.dr,0)= 0 ");
		sql.append("and nvl(tgrz_organization.dr,0)= 0 and nvl(bd_bankdoc.dr,0)= 0 and nvl(org_financeorg.dr,0)= 0 ");
		//项目ID
		if (queryValueMap.get("projectname") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.pk_projectdata",
							queryValueMap.get("projectname").split(",")));
		}
		//分期主键
		if (queryValueMap.get("periodizationname") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"cdm_contract.vdef3",
							queryValueMap.get("periodizationname").split(",")));
		}
		//项目公司
		if (queryValueMap.get("projectcorp") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"org_financeorg.pk_financeorg",
							queryValueMap.get("projectcorp").split(",")));
		}
		//借款人
		if (queryValueMap.get("borrower") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"org_financeorg.pk_financeorg",
							queryValueMap.get("borrower").split(",")));
		}
		//融资类型
				if (queryValueMap.get("pk_fintype") != null) {
//					sql.append(" and tgrz_fintype.pk_fintype in "+queryValueMap.get("pk_fintype")+"");
					sql.append(" and "
							+ SQLUtil.buildSqlForIn(
									"tgrz_fintype.pk_fintype",
									queryValueMap.get("pk_fintype").split(",")));
				}
		//融资机构
		if (queryValueMap.get("pk_organization") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"cdm_contract.pk_rzjg",
							queryValueMap.get("pk_organization").split(",")));
		}
//		sql.append(" and cdm_contract_exec.payamount is not null ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		sql.append("and nvl(tgrz_fintype.dr,0)= 0 and nvl(tgrz_OrganizationType.dr,0)= 0) rzxx ");
		sql.append("where (rzxx.年度 is not null or rzxx.年度 <> '~') ");
//		sql.append(" and (nvl(rzxx.当年放款金额, 0) > 0 or nvl(rzxx.上年放款金额, 0) > 0 )");
		sql.append("group by ");
		sql.append("rzxx.年度, ");
		sql.append("rzxx.合同起始日期, ");
		sql.append("rzxx.合同主键, ");
		sql.append("rzxx.项目ID, ");
		sql.append("rzxx.项目编码, ");
		sql.append("rzxx.项目名称, ");
		sql.append("rzxx.财务顾问费率, ");
		sql.append("rzxx.项目公司, ");
		sql.append("rzxx.借款人, ");
		sql.append("rzxx.融资类型, ");
		sql.append("rzxx.融资类型编码, ");
		sql.append("rzxx.融资类型名称, ");
		sql.append("rzxx.融资机构, ");
		sql.append("rzxx.融资机构编码, ");
		sql.append("rzxx.融资机构名称, ");
		sql.append("rzxx.融资机构类别, ");
		sql.append("rzxx.融资机构类别编码, ");
		sql.append("rzxx.融资机构类别名称, ");
		sql.append("rzxx.机构分行, ");
		sql.append("rzxx.机构支行, ");
		sql.append("rzxx.合同起始日期, ");
		sql.append("rzxx.合同结束日期, ");
		sql.append("rzxx.合同利率, ");
		sql.append("rzxx.分期主键, ");
		sql.append("rzxx.分期名称, ");
		sql.append("rzxx.区域公司名称, ");
		sql.append("rzxx.综合利率, ");
		sql.append("rzxx.合同编号, ");
		sql.append("nvl(rzxx.融资金额,0) ");
		sql.append(" ) hz ");
		sql.append(" where hz.loan_thisyearamount > 0 ");
		sql.append(" order by  hz.contractcode");
		Collection<FinYearLoanVO> coll = (Collection<FinYearLoanVO>) getBaseDAO()
		.executeQuery(sql.toString(),new BeanListProcessor(FinYearLoanVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinYearLoanVO[0]):null;
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
			if(condVO.getSQLStr()==null){
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}else{
			queryValueMap.put(condVO.getFieldCode(),
					condVO.getValue().replace("(", "").replace(")", "")
							.replace("'", ""));
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}
		}
	}
	
	
}
