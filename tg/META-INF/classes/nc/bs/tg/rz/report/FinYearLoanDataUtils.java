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
 * ������ʷſ��
 * 
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:42:09
 */
public class FinYearLoanDataUtils extends ReportUtils {
	static FinYearLoanDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql

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
	 * ��ѯ����sql
	 * add by tjl 2019-08-01
	 * @throws DAOException 
	 */
	private FinYearLoanVO[] getFinYearLoanListVOs() throws BusinessException {
		//������
//		if(conditionVOs==null||conditionVOs.length<=0){
//			return null;
//		}
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ( ");
		sql.append("select ");
		sql.append("rzxx.��� as cyear, ");
		sql.append("rzxx.��ͬ���� as pk_contract, ");
		sql.append("rzxx.��ͬ��� as contractcode, ");
		sql.append("rzxx.��ĿID as pk_project, ");//
		sql.append("to_char(rzxx.������ʷ���,'fm99999990.000099')||'%' as i_cwgwfl, ");
		sql.append("rzxx.��Ŀ���� as projectcode, ");
		sql.append("rzxx.����˾���� as pk_sxqy, ");
		sql.append("rzxx.��Ŀ���� as projectname, ");
		sql.append("rzxx.�������� as pk_periodization, ");
		sql.append("rzxx.�������� as periodizationname, ");
		sql.append("rzxx.��Ŀ��˾ as projectcorp, ");
		sql.append("rzxx.����� as borrower, ");
		sql.append("rzxx.�������� as pk_fintype, ");
		sql.append("rzxx.�������ͱ��� as fintypecode, ");
		sql.append("rzxx.������������ as fintypename, ");
		sql.append("rzxx.���ʻ��� as pk_organization, ");
		sql.append("rzxx.���ʻ������� as organizationcode, ");
		sql.append("rzxx.���ʻ������� as organizationname, ");
		sql.append("rzxx.���ʻ������ as pk_organizationtype, ");
		sql.append("rzxx.���ʻ��������� as organizationtypecode, ");
		sql.append("rzxx.���ʻ���������� as organizationtypename, ");
		sql.append("rzxx.�������� as branch, ");
		sql.append("rzxx.����֧�� as subbranch, ");
		sql.append("rzxx.��ͬ��ʼ���� as begindate, ");
		sql.append("rzxx.��ͬ�������� as enddate, ");
		sql.append("nvl(rzxx.���ʽ��,0) as fin_amount, ");
		sql.append("sum(nvl(rzxx.�ۼƷſ���,0)) as loan_totalamount, ");
		sql.append("sum(nvl(rzxx.����ſ���,0)) as loan_lastyearamount, ");
		sql.append("sum(nvl(rzxx.����ſ���,0)) as loan_thisyearamount, ");
		sql.append("nvl(rzxx.���ʽ��, 0) - sum(nvl(rzxx.�ۼƷſ���, 0)) as loan_awaitamount, ");
		sql.append("to_char(rzxx.��ͬ����,'fm99999990.000099')||'%' as cont_lv, ");
		sql.append("rzxx.�ۺ�����||'%' as multiple_lv, ");
		//12c�������ת�к���
//		sql.append("to_char(wmsys.wm_concat(rzxx.t)) as loan_datenote, ");
		//11g�������ת�к���
		sql.append("to_char(listagg(rzxx.t, ',') within GROUP(order by rzxx.t)) as loan_datenote, ");
		sql.append("sum(nvl(rzxx.\"1�·ſ���\",0)) as jan_amount, ");
		sql.append("sum(nvl(rzxx.\"2�·ſ���\",0)) as feb_amount, ");
		sql.append("sum(nvl(rzxx.\"3�·ſ���\",0)) as mar_amount, ");
		sql.append("sum(nvl(rzxx.\"4�·ſ���\",0)) as apr_amount, ");
		sql.append("sum(nvl(rzxx.\"5�·ſ���\",0)) as may_amount, ");
		sql.append("sum(nvl(rzxx.\"6�·ſ���\",0)) as jun_amount, ");
		sql.append("sum(nvl(rzxx.\"7�·ſ���\",0)) as jul_amount, ");
		sql.append("sum(nvl(rzxx.\"8�·ſ���\",0)) as aug_amount, ");
		sql.append("sum(nvl(rzxx.\"9�·ſ���\",0)) as sep_amount, ");
		sql.append("sum(nvl(rzxx.\"10�·ſ���\",0)) as oct_amount, ");
		sql.append("sum(nvl(rzxx.\"11�·ſ���\",0)) as nov_amount, ");
		sql.append("sum(nvl(rzxx.\"12�·ſ���\",0)) as dec_amount ");
			sql.append("from ( ");
				sql.append("select  ");
				
		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as ���, ");
		sql.append("cdm_contract.pk_contract as ��ͬ����, ");
		sql.append("cdm_contract.contractcode as ��ͬ���, ");
		sql.append(" ROUND((nvl(cdm_contract.i_cwgwfl, 0) * 100),4)+nvl(cdm_contract.pk_htlv,0) as �ۺ�����, ");
		sql.append(" ROUND((nvl(cdm_contract.i_cwgwfl, 0) * 100),4) as ������ʷ���, ");
		sql.append("cdm_contract.pk_sxqy as ����˾����, ");
		sql.append("org_orgs.name as ����˾����, ");
		sql.append("tgrz_projectdata.pk_projectdata as ��ĿID, ");
		sql.append("tgrz_projectdata.code as ��Ŀ����, ");
		sql.append("tgrz_projectdata.name as ��Ŀ����, ");
		sql.append("cdm_contract.vdef3 ��������, ");
		sql.append("tgrz_projectdata_c.periodizationname as ��������, ");
		sql.append("tgrz_organization.branch as ��������, ");
		sql.append("tgrz_organization.subbranch as ����֧��, ");
		sql.append("org_financeorg.pk_financeorg as ��Ŀ��˾pk, ");
		sql.append("tgrz_projectdata.projectcorp as ��Ŀ��˾, ");
		sql.append("org_financeorg.name as �����, ");
		sql.append("cdm_contract.pk_rzlx as ��������, ");
		sql.append("tgrz_fintype.code as �������ͱ���, ");
		sql.append("tgrz_fintype.name as ������������, ");
		sql.append("cdm_contract.pk_rzjg as ���ʻ���, ");
		sql.append("tgrz_organization.code as ���ʻ�������, ");
		sql.append("tgrz_organization.name as ���ʻ�������, ");
		sql.append("cdm_contract.pk_rzjglb as ���ʻ������, ");
		sql.append("tgrz_OrganizationType.code as ���ʻ���������, ");
		sql.append("tgrz_OrganizationType.name as ���ʻ����������, ");
		sql.append("(substr(cdm_contract.begindate,1,11)) as ��ͬ��ʼ����, ");
		sql.append("(substr(cdm_contract.enddate,1,11)) as ��ͬ��������, ");
		sql.append("cdm_contract.contractamount as ���ʽ��, ");
		sql.append("(case when substr(cdm_contract_exec.busidate, 1, 4) < '"+queryValueMap.get("cyear")+"' then cdm_contract_exec.payamount else 0 end)+ ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,10) >= '"+queryValueMap.get("cyear")+"-01-01' and substr(cdm_contract_exec.busidate,1,10) <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else  0  end)  as �ۼƷſ���, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,4) = '"+queryValueMap.get("cyear")+"' - '1' then cdm_contract_exec.payamount else 0 end ) as ����ſ���, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,4) = '"+queryValueMap.get("cyear")+"'  then cdm_contract_exec.payamount else 0 end) as ����ſ���, ");
//		sql.append("cdm_contract_exec.leftpayamount as ���ſ���, ");
		sql.append("nvl(cdm_contract.pk_htlv,0) as ��ͬ����, ");
		sql.append("(case when cdm_contract_exec.payamount is null then null else ");
		sql.append("substr(cdm_contract_exec.busidate, 1, 11) || ' �ſ�' || to_char(cdm_contract_exec.payamount * 0.0001, ");
		sql.append("'fm99999990.009999') || '��Ԫ' end) as t, ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-01' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-02' then cdm_contract_exec.payamount else 0 end) as \"1�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-02' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-03' then cdm_contract_exec.payamount else 0 end) as \"2�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-03' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-04' then cdm_contract_exec.payamount else 0 end) as \"3�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-04' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-05' then cdm_contract_exec.payamount else 0 end) as \"4�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-05' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-06' then cdm_contract_exec.payamount else 0 end) as \"5�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-06' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-07' then cdm_contract_exec.payamount else 0 end) as \"6�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-07' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-08' then cdm_contract_exec.payamount else 0 end) as \"7�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-08' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-09' then cdm_contract_exec.payamount else 0 end) as \"8�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-09' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-10' then cdm_contract_exec.payamount else 0 end) as \"9�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-10' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-11' then cdm_contract_exec.payamount else 0 end) as \"10�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) >= "+queryValueMap.get("cyear")+"||'-11' and substr(cdm_contract_exec.busidate,1,7) < "+queryValueMap.get("cyear")+"||'-12' then cdm_contract_exec.payamount else 0 end) as \"11�·ſ���\", ");
		sql.append("(case when substr(cdm_contract_exec.busidate,1,7) = "+queryValueMap.get("cyear")+"||'-12'  then cdm_contract_exec.payamount else 0 end) as \"12�·ſ���\" ");
		
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
		//��ĿID
		if (queryValueMap.get("projectname") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.pk_projectdata",
							queryValueMap.get("projectname").split(",")));
		}
		//��������
		if (queryValueMap.get("periodizationname") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"cdm_contract.vdef3",
							queryValueMap.get("periodizationname").split(",")));
		}
		//��Ŀ��˾
		if (queryValueMap.get("projectcorp") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"org_financeorg.pk_financeorg",
							queryValueMap.get("projectcorp").split(",")));
		}
		//�����
		if (queryValueMap.get("borrower") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"org_financeorg.pk_financeorg",
							queryValueMap.get("borrower").split(",")));
		}
		//��������
				if (queryValueMap.get("pk_fintype") != null) {
//					sql.append(" and tgrz_fintype.pk_fintype in "+queryValueMap.get("pk_fintype")+"");
					sql.append(" and "
							+ SQLUtil.buildSqlForIn(
									"tgrz_fintype.pk_fintype",
									queryValueMap.get("pk_fintype").split(",")));
				}
		//���ʻ���
		if (queryValueMap.get("pk_organization") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"cdm_contract.pk_rzjg",
							queryValueMap.get("pk_organization").split(",")));
		}
//		sql.append(" and cdm_contract_exec.payamount is not null ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		sql.append("and nvl(tgrz_fintype.dr,0)= 0 and nvl(tgrz_OrganizationType.dr,0)= 0) rzxx ");
		sql.append("where (rzxx.��� is not null or rzxx.��� <> '~') ");
//		sql.append(" and (nvl(rzxx.����ſ���, 0) > 0 or nvl(rzxx.����ſ���, 0) > 0 )");
		sql.append("group by ");
		sql.append("rzxx.���, ");
		sql.append("rzxx.��ͬ��ʼ����, ");
		sql.append("rzxx.��ͬ����, ");
		sql.append("rzxx.��ĿID, ");
		sql.append("rzxx.��Ŀ����, ");
		sql.append("rzxx.��Ŀ����, ");
		sql.append("rzxx.������ʷ���, ");
		sql.append("rzxx.��Ŀ��˾, ");
		sql.append("rzxx.�����, ");
		sql.append("rzxx.��������, ");
		sql.append("rzxx.�������ͱ���, ");
		sql.append("rzxx.������������, ");
		sql.append("rzxx.���ʻ���, ");
		sql.append("rzxx.���ʻ�������, ");
		sql.append("rzxx.���ʻ�������, ");
		sql.append("rzxx.���ʻ������, ");
		sql.append("rzxx.���ʻ���������, ");
		sql.append("rzxx.���ʻ����������, ");
		sql.append("rzxx.��������, ");
		sql.append("rzxx.����֧��, ");
		sql.append("rzxx.��ͬ��ʼ����, ");
		sql.append("rzxx.��ͬ��������, ");
		sql.append("rzxx.��ͬ����, ");
		sql.append("rzxx.��������, ");
		sql.append("rzxx.��������, ");
		sql.append("rzxx.����˾����, ");
		sql.append("rzxx.�ۺ�����, ");
		sql.append("rzxx.��ͬ���, ");
		sql.append("nvl(rzxx.���ʽ��,0) ");
		sql.append(" ) hz ");
		sql.append(" where hz.loan_thisyearamount > 0 ");
		sql.append(" order by  hz.contractcode");
		Collection<FinYearLoanVO> coll = (Collection<FinYearLoanVO>) getBaseDAO()
		.executeQuery(sql.toString(),new BeanListProcessor(FinYearLoanVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinYearLoanVO[0]):null;
	}

	/**
	 * ��ʼ����ѯ������Ϣ
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
