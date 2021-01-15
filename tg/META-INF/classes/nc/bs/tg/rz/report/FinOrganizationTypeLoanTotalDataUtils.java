package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinOrganizationTypeLoanTotalVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;
/**
 * ���ʻ������ͷſ�ͳ��


 * @author HUANGDQ
 * @date 2019��7��3�� ����3:46:39
 */
public class FinOrganizationTypeLoanTotalDataUtils extends ReportUtils {
	static FinOrganizationTypeLoanTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql

	public static FinOrganizationTypeLoanTotalDataUtils getUtils() {
		if (utils == null) {
			utils = new FinOrganizationTypeLoanTotalDataUtils();
		}
		return utils;
	}

	public FinOrganizationTypeLoanTotalDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinOrganizationTypeLoanTotalVO()));
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

			FinOrganizationTypeLoanTotalVO [] vos = getFinTypeLoanTotalVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private FinOrganizationTypeLoanTotalVO[] getFinTypeLoanTotalVOs() throws BusinessException{
		// TODO �Զ����ɵķ������
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
//		sql.append("rzjglb.��� as cyear, ");
//		sql.append("rzjglb.���ʻ���������� as pk_organizationtype, ");
		//sql.append("rzjglb.���ʻ������� as fintypename, ");
//		sql.append("rzjglb.���ʻ��������� as organizationetypecode, ");
		sql.append("rzjglb.���ʻ���������� as organizationtypename, ");
		sql.append("sum(nvl(rzjglb.��ȷſ���,0)) as amount ");
			sql.append("from ( ");
		sql.append("select ");
//		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as ���, ");
		sql.append("cdm_contract.pk_rzjglb as ���ʻ����������, ");
		sql.append("cdm_contract.pk_rzjg as ���ʻ�������, ");
		sql.append("tgrz_OrganizationType.Code as ���ʻ���������, ");
		sql.append("tgrz_OrganizationType.Name as ���ʻ����������, ");
		sql.append("tgrz_organization.code as ���ʻ�������, ");
		sql.append("tgrz_organization.name as ���ʻ�������, ");
		sql.append("(case when cdm_contract_exec.busidate >= '"+queryValueMap.get("cyear")+"-01-01' and cdm_contract_exec.busidate <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else 0 end ) as ��ȷſ��� ");
		sql.append("from tgrz_OrganizationType ");
		sql.append("left join tgrz_organization on tgrz_OrganizationType.Pk_Organizationtype = tgrz_organization.pk_organizationtype ");
		sql.append("left join cdm_contract on tgrz_organization.pk_organization = cdm_contract.pk_rzjg ");
		sql.append("left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
//		sql.append("where  cdm_contract.pk_rzjglb <> '~' ");
		sql.append(" where  nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" and nvl(cdm_contract_exec.dr,0) = 0 ");
		sql.append(" and nvl(tgrz_OrganizationType.dr,0) = 0 ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		sql.append(") rzjglb ");
		sql.append("where nvl(rzjglb.��ȷſ���,0)>0 ");
		sql.append("group by ");
//		sql.append("rzjglb.���, ");
//		sql.append("rzjglb.���ʻ����������, ");
//		sql.append("rzjglb.���ʻ���������, ");
		sql.append("rzjglb.���ʻ���������� ");
		//sql.append("rzjglb.���ʻ������� ");
		sql.append("order by  ");
		sql.append(" decode(rzjglb.���ʻ����������, '�����ʹ�', 1, '�����', 2, 'ȫ���ɷ�������', 3, '����ڻ���', 4, '��������', 99) ");
		Collection<FinOrganizationTypeLoanTotalVO> coll = (Collection<FinOrganizationTypeLoanTotalVO>)
				getBaseDAO()
				.executeQuery(sql.toString(),new BeanListProcessor(FinOrganizationTypeLoanTotalVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinOrganizationTypeLoanTotalVO[0]):null;
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
			queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}
}
