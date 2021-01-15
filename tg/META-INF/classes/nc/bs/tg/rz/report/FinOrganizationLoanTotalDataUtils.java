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
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinOrganizationLoanTotalVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * ���ʻ����ſ�ͳ��
 * 
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:46:25
 */
public class FinOrganizationLoanTotalDataUtils extends ReportUtils {
	static FinOrganizationLoanTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql

	public static FinOrganizationLoanTotalDataUtils getUtils() {
		if (utils == null) {
			utils = new FinOrganizationLoanTotalDataUtils();
		}
		return utils;
	}

	public FinOrganizationLoanTotalDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinOrganizationLoanTotalVO()));
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
			
			FinOrganizationLoanTotalVO [] vos = getFinOrganizationLoanTotalVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private FinOrganizationLoanTotalVO[] getFinOrganizationLoanTotalVOs() throws DAOException {
		StringBuffer sql = new StringBuffer();
		sql.append("select  ");
//		sql.append("rzjg.���ʻ������� as pk_organization, ");
//		sql.append("rzjg.���ʻ������� as organizationecode, ");
		sql.append("rzjg.���ʻ������� as organizationname, ");
		sql.append("sum(nvl(rzjg.��ȷſ���, 0)) as amount ");
		sql.append(" from ( select ");
//			sql.append("from (select (case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as ���, ");
//		sql.append("tgrz_organization.pk_organization as ���ʻ�������, ");
//		sql.append("tgrz_organization.code as ���ʻ�������, ");
		sql.append("tgrz_organization.name as ���ʻ�������, ");
//		sql.append("(case when substr(cdm_contract_exec.busidate, 1, 4) in "+queryValueMap.get("cyear")+" then cdm_contract_exec.payamount else 0 end) as ��ȷſ��� ");
		sql.append("(case when cdm_contract_exec.busidate >= '"+queryValueMap.get("cyear")+"-01-01' and cdm_contract_exec.busidate <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else 0 end ) as ��ȷſ��� ");
		sql.append("from cdm_contract ");
		sql.append("left join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg ");
		sql.append("left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("where cdm_contract.pk_rzjg <> '~' ");
		sql.append(" and nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" and nvl(cdm_contract_exec.dr,0) = 0 ");
		sql.append(" and nvl(tgrz_organization.dr,0) = 0 ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		//���ʻ���
		if (queryValueMap.get("pk_organization") != null) {
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.name",
							queryValueMap.get("pk_organization").split(",")));
//					sql.append(" where tgrz_organization.pk_organization in "+queryValueMap.get("pk_organization")+"");
		}
		sql.append(") rzjg ");
		sql.append(" where nvl(rzjg.��ȷſ���,0)>0 ");
		sql.append(" group by ");
//		sql.append("rzjg.���, ");
//		sql.append("rzjg.���ʻ�������, ");
//		sql.append("rzjg.���ʻ�������, ");
		sql.append("rzjg.���ʻ������� ");
		Collection<FinOrganizationLoanTotalVO> coll = (Collection<FinOrganizationLoanTotalVO>) getBaseDAO()
				.executeQuery(sql.toString(),new BeanListProcessor(FinOrganizationLoanTotalVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinOrganizationLoanTotalVO[0]):null;
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
			if (condVO.getValue() != null && !"".equals(condVO.getValue())) {
				if (condVO.getDataType() == ConditionVO.DATE) {
					String[] dates = condVO.getValue().split("@@");
					queryValueMap.put(condVO.getFieldCode() + "_begin",
							new UFDate(dates[0]).asBegin().toString());
					queryValueMap.put(condVO.getFieldCode() + "_end",
							new UFDate(dates[dates.length - 1]).asEnd()
							.toString());
					
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
