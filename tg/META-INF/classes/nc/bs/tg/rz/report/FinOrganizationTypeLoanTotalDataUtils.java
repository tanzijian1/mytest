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
 * 融资机构类型放款统计


 * @author HUANGDQ
 * @date 2019年7月3日 下午3:46:39
 */
public class FinOrganizationTypeLoanTotalDataUtils extends ReportUtils {
	static FinOrganizationTypeLoanTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

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
		// TODO 自动生成的方法存根
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
//		sql.append("rzjglb.年度 as cyear, ");
//		sql.append("rzjglb.融资机构类别主键 as pk_organizationtype, ");
		//sql.append("rzjglb.融资机构名称 as fintypename, ");
//		sql.append("rzjglb.融资机构类别编码 as organizationetypecode, ");
		sql.append("rzjglb.融资机构类别名称 as organizationtypename, ");
		sql.append("sum(nvl(rzjglb.年度放款金额,0)) as amount ");
			sql.append("from ( ");
		sql.append("select ");
//		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as 年度, ");
		sql.append("cdm_contract.pk_rzjglb as 融资机构类别主键, ");
		sql.append("cdm_contract.pk_rzjg as 融资机构主键, ");
		sql.append("tgrz_OrganizationType.Code as 融资机构类别编码, ");
		sql.append("tgrz_OrganizationType.Name as 融资机构类别名称, ");
		sql.append("tgrz_organization.code as 融资机构编码, ");
		sql.append("tgrz_organization.name as 融资机构名称, ");
		sql.append("(case when cdm_contract_exec.busidate >= '"+queryValueMap.get("cyear")+"-01-01' and cdm_contract_exec.busidate <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else 0 end ) as 年度放款金额 ");
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
		sql.append("where nvl(rzjglb.年度放款金额,0)>0 ");
		sql.append("group by ");
//		sql.append("rzjglb.年度, ");
//		sql.append("rzjglb.融资机构类别主键, ");
//		sql.append("rzjglb.融资机构类别编码, ");
		sql.append("rzjglb.融资机构类别名称 ");
		//sql.append("rzjglb.融资机构名称 ");
		sql.append("order by  ");
		sql.append(" decode(rzjglb.融资机构类别名称, '信托资管', 1, '五大行', 2, '全国股份制银行', 3, '类金融机构', 4, '其他银行', 99) ");
		Collection<FinOrganizationTypeLoanTotalVO> coll = (Collection<FinOrganizationTypeLoanTotalVO>)
				getBaseDAO()
				.executeQuery(sql.toString(),new BeanListProcessor(FinOrganizationTypeLoanTotalVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinOrganizationTypeLoanTotalVO[0]):null;
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
			queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
			queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
		}
	}
}
