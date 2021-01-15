package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.OrganizationApprovalTotalVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 各金融机构授信使用情况
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:54:16
 */
public class OrganizationApprovalTotalDataUtils extends ReportUtils {
	static OrganizationApprovalTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static OrganizationApprovalTotalDataUtils getUtils() {
		if (utils == null) {
			utils = new OrganizationApprovalTotalDataUtils();
		}
		return utils;
	}

	public OrganizationApprovalTotalDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new OrganizationApprovalTotalVO()));
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

			OrganizationApprovalTotalVO[] vos = getOrganizationApprovalTotalVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private OrganizationApprovalTotalVO[] getOrganizationApprovalTotalVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" maindata.organizationname organizationname, ");//机构名称
		sql.append(" maindata.total_amount total_amount, ");//授信总额
		sql.append(" maindata.total_amount - maindata.use_amount notuse_amount, ");// 未使用授信额度
		sql.append(" maindata.nonwithdrawal_amount nonwithdrawal_amount, ");// 已批复末提款金额
		sql.append(" maindata.use_amount use_amount, ");// 授信使用额度
		sql.append(" case when nvl(maindata.total_amount,0) = 0 then 0 else nvl(maindata.use_amount, 0)/nvl(maindata.total_amount,0) end use_ratio, ");// 授信使用占比
		sql.append(" datetime.replydate replydate, ");// 授信批复日
		sql.append(" datetime.expiredate expiredate ");// 授信到期日
		sql.append(" from  ");
		sql.append(" (select  ");
		sql.append("  v3.organizationname organizationname, ");
		sql.append("  sum(nvl(v3.total_amount,0)) total_amount, ");
		sql.append("  sum(nvl(v3.total_amount,0)) - sum(nvl(v3.use_amount,0)) notuse_amount, ");
		sql.append("  sum(nvl(v3.nonwithdrawal_amount,0)) nonwithdrawal_amount, ");
		sql.append("  nvl(v3.use_amount,0) use_amount, ");
		sql.append("  0 use_ratio ");
		sql.append("  from(  ");
		sql.append("  select tgrz_organization.name organizationname, ");
		sql.append("  sum(nvl(cc_bankprotocol.replyunlentamount, 0)) nonwithdrawal_amount, ");
		sql.append("  nvl(cc_bankprotocol.cdtlnamt, 0) total_amount, ");
		sql.append("  nvl(v2.use_amount, 0) use_amount, ");
		sql.append("  0 use_ratio ");
		sql.append("   from tgrz_organization ");
		sql.append("   left join cc_bankprotocol on cc_bankprotocol.vbillstatus = 1 and cc_bankprotocol.dr = 0 and cc_bankprotocol.vdef7 = tgrz_organization.pk_organization ");
//		sql.append("   and tgrz_fisceme.pk_scheme in");
//		sql.append("   (select tgrz_fischeme_b.pk_scheme ");
//		sql.append("    from tgrz_fischeme_b ");
//		sql.append("    where tgrz_fischeme_b.isdrawescheme <> 'Y' ");
//		sql.append("    and tgrz_fischeme_b.dr = 0)");
		sql.append("   left join ");
		sql.append("   (select (sum(nvl(cdm_contract_exec.payamount, 0)) - sum(nvl(cdm_contract_exec.repayamount, 0))) use_amount, ");
		sql.append("    tgrz_organization.name organizationname");
		sql.append("     from cdm_contract");
		sql.append("    left join tgrz_organization on tgrz_organization.pk_organization = cdm_contract.pk_rzjg and tgrz_organization.dr = 0 ");
		sql.append("    left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("    where cdm_contract.contstatus = 'EXECUTING' ");
		sql.append("    and cdm_contract.vbillstatus = 1  ");
		sql.append("    and cdm_contract.dr = 0 ");
		sql.append("    and cdm_contract_exec.dr = 0");
		sql.append("    and cdm_contract.pk_contract in (  ");
		sql.append("    select cdm_contract.pk_contract from  ");
		sql.append("    cdm_contract   ");
		sql.append("    inner join cdm_sxzyqk on cdm_sxzyqk.pk_contract = cdm_contract.pk_contract and cdm_sxzyqk.dr = 0  ");
		sql.append("    where cdm_contract.dr = 0  ");
		sql.append("    group by cdm_contract.pk_contract  ");
		sql.append("    )  ");
		sql.append("    group by tgrz_organization.name) v2");
		sql.append("   on tgrz_organization.name = v2.organizationname ");
		sql.append("  where tgrz_organization.dr = 0 ");
		
		if (queryValueMap.get("pk_organization") != null) {// 年度
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.pk_organization", queryValueMap
									.get("pk_organization").split(",")));
		}
		
		
		sql.append("   group by tgrz_organization.name, ");
		sql.append("   nvl(cc_bankprotocol.cdtlnamt, 0), ");
		sql.append("   nvl(v2.use_amount, 0)) v3");
		sql.append("   group by ");
		sql.append("   v3.organizationname, ");
		sql.append("   nvl(v3.use_amount,0)) maindata ");
		sql.append(" left join   ");
		sql.append(" (select   ");
		sql.append("   tgrz_organization.name,");
		sql.append("   substr(cc_bankprotocol.begindate,0,10)  replydate, ");
		sql.append("   substr(cc_bankprotocol.enddate,0,10)  expiredate ");
		sql.append("   from tgrz_organization ");
		sql.append("   left join cc_bankprotocol on cc_bankprotocol.vbillstatus = 1 and cc_bankprotocol.dr = 0 and cc_bankprotocol.vdef7 = tgrz_organization.pk_organization ");
		sql.append("   where cc_bankprotocol.begindate is not null or cc_bankprotocol.enddate is not null ");
		sql.append("   ) datetime on datetime.name = maindata.organizationname ");
		sql.append("   where maindata.total_amount > 0 ");
		if (queryValueMap.get("replydate") != null) {// 授信批复日
			sql.append(" and datetime.replydate >= '"+queryValueMap.get("replydate").substring(0,11)+"' ");
		}else {
			sql.append(" and datetime.replydate >= '1970-01-01' ");
		}
		if (queryValueMap.get("expiredate") != null) {// 授信到期日
			sql.append(" and datetime.expiredate <= '"+queryValueMap.get("expiredate").substring(0,11)+"' ");
		}else {
			sql.append(" and datetime.expiredate <= '9999-01-01' ");
		}
//		sql.append("  group by  tgrz_organization.code,")//
//		.append("  tgrz_organization.name,")
//		.append("  substr(cc_bankprotocol.begindate,0,10),")
//		.append("  substr(cc_bankprotocol.enddate,0,10),")
//		.append("  cc_bankprotocol.availcdtlnamt,")
//		.append(" nvl(v2.use_amount, 0), ")
//		.append("  nvl(cc_bankprotocol.cdtlnamt, 0),")
//		.append("  cc_bankprotocol.curusdcdtlnamt/cc_bankprotocol.cdtlnamt ");
		//sql.append(" group by tgrz_organization.pk_organization,tgrz_organization.code,tgrz_organization.name,cc_bankprotocol.availcdtlnamt,cc_bankprotocol.replyunlentamount, cc_bankprotocol.cdtlnamt, cc_bankprotocol.begindate,cc_bankprotocol.enddate");

		Collection<OrganizationApprovalTotalVO> coll = (Collection<OrganizationApprovalTotalVO>) getBaseDAO()
				.executeQuery(
						sql.toString(),
						new BeanListProcessor(OrganizationApprovalTotalVO.class));

		return coll != null && coll.size() > 0 ? coll
				.toArray(new OrganizationApprovalTotalVO[0]) : null;
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
