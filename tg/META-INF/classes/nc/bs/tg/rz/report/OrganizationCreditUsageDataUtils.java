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
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.OrganizationCreditUsageVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 各金融机构报批汇总表
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:54:59
 */
public class OrganizationCreditUsageDataUtils extends ReportUtils {
	static OrganizationCreditUsageDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql
	Map<String, UFDouble> amoutMap = new HashMap<String, UFDouble>();

	public static OrganizationCreditUsageDataUtils getUtils() {
		if (utils == null) {
			utils = new OrganizationCreditUsageDataUtils();
		}
		return utils;
	}

	public OrganizationCreditUsageDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new OrganizationCreditUsageVO()));
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

			OrganizationCreditUsageVO[] vos = getOrganizationCreditUsageVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	@SuppressWarnings("unchecked")
	private OrganizationCreditUsageVO[] getOrganizationCreditUsageVOs()
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select v5.organizationname")
				.append(", v5.projectcode  ")
				// 项目编码
				.append(", v5.projectname  ")
				// 项目名称
				.append(", v5.fin_amount  ")
				.append(", v5.fin_amount approval_amount  ")//正在报批金额
				.append(",v4.total_amount")
				.append(",v4.use_amount ")
				.append(" from (")
				
				.append("  select ")
				.append("tgrz_organization.name organizationname ")
				.append(",tgrz_fisceme.billno projectcode  ")
				// 项目编码
				.append(",tgrz_fisceme.name projectname  ")
				// 项目名称
				.append(",tgrz_fisceme.nmy fin_amount  ")
				.append(" from tgrz_organization ")
				.append(" left join tgrz_fisceme on tgrz_fisceme.pk_organization=tgrz_organization.pk_organization  and tgrz_fisceme.dr = 0  ")
//						+ " and not exists  (select tgrz_fischeme_b.pk_scheme  from tgrz_fischeme_b where  (tgrz_fischeme_b.vbdef5   is not  null and tgrz_fischeme_b.vbdef5 <> '~')  and tgrz_fischeme_b.dr = 0 and tgrz_fischeme_b.def13='实际完成' and tgrz_fischeme_b.pk_scheme = tgrz_fisceme.pk_scheme ) "
//						+ " and not exists  (select tgrz_capmarket_b.pk_scheme  from tgrz_capmarket_b where  (tgrz_capmarket_b.def7   is not  null and tgrz_capmarket_b.def7 <> '~')  and tgrz_capmarket_b.dr = 0 and tgrz_capmarket_b.def13='实际完成' and tgrz_capmarket_b.pk_scheme = tgrz_fisceme.pk_scheme ) "
//						+ " and not exists  (select tgrz_nfischeme_b.pk_scheme  from tgrz_nfischeme_b where  (tgrz_nfischeme_b.def4   is not  null and tgrz_nfischeme_b.def4 <> '~')  and tgrz_nfischeme_b.dr = 0 and tgrz_nfischeme_b.def13='实际完成' and tgrz_nfischeme_b.pk_scheme = tgrz_fisceme.pk_scheme ) ")
				.append("  where tgrz_organization.dr = 0 ")
				.append("  and (tgrz_fisceme.approvestatus = 1 or tgrz_fisceme.vdef10 = 'Y') ")//需要将变更过数算上 add by tjl 2020-11-30
				.append("  and tgrz_fisceme.disable <>'Y' ");
		if (queryValueMap.get("pk_organization") != null) {// 年度
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.pk_organization", queryValueMap
									.get("pk_organization").split(",")));
		}
		sql.append("  )v5 ");
				
				
				
		sql.append("left join (select  ")
				.append("tgrz_organization.name organizationname ")

				// 拟融资金额(亿元)
				// 融资机构名称
				.append(",sum(nvl(cc_bankprotocol.cdtlnamt, 0)) total_amount ")
				// 授信总额
				.append(",v2.use_amount use_amount ")
				// 占用授信金额数
				.append(" from tgrz_organization ")
				.append(" left join cc_bankprotocol on cc_bankprotocol.vbillstatus = 1 and cc_bankprotocol.dr = 0 and cc_bankprotocol.vdef7 = tgrz_organization.pk_organization ")
				.append(" left join (select  ")
				.append(" (sum(nvl(cdm_contract_exec.payamount, 0)) - sum(nvl(cdm_contract_exec.repayamount, 0))) use_amount, ")
				.append(" tgrz_organization.name organizationname ")
				.append(" from cdm_contract  ")
				.append(" inner join tgrz_organization on tgrz_organization.pk_organization= cdm_contract.pk_rzjg and tgrz_organization.dr = 0 ")
				.append(" left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract  ")
				.append(" where cdm_contract.contstatus in ('EXECUTING','FINISHED','OVERED')   ")
				.append("    and cdm_contract.vbillstatus = 1  ")
				.append(" and cdm_contract.dr = 0  ")
				.append(" and cdm_contract_exec.dr = 0 ")
				.append(" group by tgrz_organization.name ) v2 on tgrz_organization.name = v2.organizationname ");
		sql.append("  where tgrz_organization.dr = 0 ");

		if (queryValueMap.get("replydate_begin") != null) {// 授信批复日
			sql.append(" and cc_bankprotocol.begindate >= '"+queryValueMap.get("replydate_begin").substring(0,10)+"' ");
		}else {
			sql.append(" and cc_bankprotocol.begindate >= '1970-01-01' ");
		}
		if (queryValueMap.get("expiredate_end") != null) {// 授信到期日
			sql.append(" and cc_bankprotocol.enddate <= '"+queryValueMap.get("expiredate_end").substring(0,10)+"' ");
		}else {
			sql.append(" and cc_bankprotocol.enddate <= '9999-01-01' ");
		}
		if (queryValueMap.get("pk_organization") != null) {// 年度
			sql.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.pk_organization", queryValueMap
									.get("pk_organization").split(",")));
		}
		sql.append(" group by tgrz_organization.name,v2.use_amount )v4 ");
				
		sql.append("  on v4.organizationname = v5.organizationname ");

		StringBuffer repSQL = new StringBuffer();
		repSQL.append(
				" SELECT '报批项目'||to_char((row_number() over(partition by v1.organizationname order by v1.organizationname,v1.projectcode)),'9909')  pk_project ")
				.append(",v1.organizationname,v1.organizationname,v1.projectcode,v1.projectname,cast(v1.fin_amount/100000000 AS decimal(18, 2)) fin_amount,v1.total_amount,v1.use_amount,v1.approval_amount ")
				.append(" from (" + sql.toString() + ") v1 ");
		repSQL.append("where  v1.total_amount > 0");

		Collection<OrganizationCreditUsageVO> coll = (Collection<OrganizationCreditUsageVO>) getBaseDAO()
				.executeQuery(repSQL.toString(),
						new BeanListProcessor(OrganizationCreditUsageVO.class));
		// if(coll.size()>0){
		// //UFDouble sumFin_amout= UFDouble.ZERO_DBL;
		// for(OrganizationCreditUsageVO co:coll){
		// //co.setApproval_amount(co.getFin_amount());
		// if(amoutMap.get(co.getPk_organization()) == null){
		// amoutMap.put(co.getPk_organization(),
		// co.getFin_amount()==null?UFDouble.ZERO_DBL:co.getFin_amount());
		// }else{
		// UFDouble sumFin_amout = amoutMap.get(co.getPk_organization());
		// amoutMap.put(co.getPk_organization(),
		// sumFin_amout.add(co.getFin_amount()==null?UFDouble.ZERO_DBL:co.getFin_amount()));
		// }
		//
		// }
		// }
		// if(coll.size()>0){
		// for(OrganizationCreditUsageVO co:coll){
		// co.setApproval_amount(amoutMap.get(co.getPk_organization()));
		// }
		// amoutMap.clear();
		// }
		return coll != null && coll.size() > 0 ? coll
				.toArray(new OrganizationCreditUsageVO[0]) : null;

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
