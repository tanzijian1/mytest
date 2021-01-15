package nc.bs.tg.rz.report;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.itf.tg.rz.report.TGReprotContst;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinClosedDetailedVO;

import org.apache.commons.lang.StringUtils;

import com.ufida.dataset.IContext;

/**
 * 已结清融资明细
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:48:38
 */
public class FinClosedDetailedDataUtils extends FinBalanceAndFinCloseDetailUtil {
	static FinClosedDetailedDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql
	public static FinClosedDetailedDataUtils getUtils() {
		if (utils == null) {
			utils = new FinClosedDetailedDataUtils();
		}
		return utils;
	}

	public FinClosedDetailedDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinClosedDetailedVO()));
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(TGReprotContst.KEY_QueryScheme);
				if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}
			initQuery(conditionVOs);
			

			FinClosedDetailedVO[] vos = FinClosedDetailedVO();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private FinClosedDetailedVO[] FinClosedDetailedVO()
			throws BusinessException {
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append(""+getCommonField()+"")
		.append(",substr((select max(cdm_contract_exec.busidate) from  cdm_contract_exec where ")
		.append("cdm_contract_exec.pk_contract = cdm_contract.pk_contract ")
		.append("and cdm_contract_exec.summary = 'HBJ'),0,10) settledate ")
		 
                  
                                 
                             
		//.append(",substr(max(cdm_contract_exec.busidate),0,10) settledate ")// 放款结束日
		.append(""+getLeftTable()+" ")
				/*.append(" from cdm_contract left join tgrz_projectdata on tgrz_projectdata.pk_projectdata=cdm_contract.pk_project ")
				.append(" left join tgrz_fintype on tgrz_fintype.pk_fintype=cdm_contract.pk_rzlx ")
				.append(" left join tgrz_organization on tgrz_organization.pk_organization=cdm_contract.pk_rzjg")
				.append(" left join cdm_contract_exec on cdm_contract_exec.pk_contract=cdm_contract.pk_contract")*/
//				.append(" left join tgrz_projectdata_c on tgrz_projectdata.pk_projectdata=tgrz_projectdata_c.pk_projectdata")
		.append(" where  cdm_contract.vbillstatus='1' ")//OVERED FINISHED	
		.append(" and cdm_contract.pk_contract in (select cdm_contract_exec.pk_contract from cdm_contract_exec where cdm_contract_exec.leftrepayamount = 0 )");
//		.append(" and cdm_contract.pk_contract in "
//						+ " (select pk_contract from cdm_contract_exec group by pk_contract  "
//						+ " having sum(nvl(payamount,0)) = sum(nvl(repayamount,0))) ");
		
		/*if (queryValueMap.get("fin_year") != null) {// 年度
			sqlBuff.append(" and substr(cdm_contract_exec.busidate,1,4) in "+queryValueMap.get("fin_year")+"");
		}*/
		if (queryWhereMap.get("pk_projectdata_c") != null) {// 分期名称
//			if(queryValueMap.get("pk_projectdata_c").length()<=20){
//				sqlBuff.append(" and tgrz_projectdata_c.pk_projectdata_c in '"+queryValueMap.get("pk_projectdata_c")+"'");
//				
//			}else{
//				sqlBuff.append(" and tgrz_projectdata_c.pk_projectdata_c in "+queryValueMap.get("pk_projectdata_c"));
//			}
			
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata_c.pk_projectdata_c",
							queryValueMap.get("pk_projectdata_c").split(",")));
		}
		if (queryValueMap.get("pk_projectdata") != null) {// 项目名称
//			if(queryValueMap.get("pk_projectdata").length()<=20){
//				sqlBuff.append(" and tgrz_projectdata.pk_projectdata in '"+queryValueMap.get("pk_projectdata")+"'");
//				
//			}else{
//				sqlBuff.append(" and tgrz_projectdata.pk_projectdata in"+queryValueMap.get("pk_projectdata"));
//				
//			}
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.pk_projectdata",
							queryValueMap.get("pk_projectdata").split(",")));
			
			//sqlBuff.append(queryValueMap.get("pk_projectdata"));
		}
		if (queryWhereMap.get("pk_financeorg") != null) {// 项目公司名称
//			if(queryValueMap.get("pk_financeorg").length()<=20){
//				sqlBuff.append(" and tgrz_projectdata.projectcorp in '"+queryValueMap.get("pk_financeorg")+"'");
//				
//			}else{
//				sqlBuff.append(" and tgrz_projectdata.projectcorp in "+queryValueMap.get("pk_financeorg"));
//				
//			}
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_projectdata.projectcorp",
							queryValueMap.get("pk_financeorg").split(",")));
			
		}
		if (queryValueMap.get("borrower") != null) {// 借款人
			/*sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn("pk_financeorg",
							queryValueMap.get("borrower").split(",")));*/
//			if(queryValueMap.get("borrower").length()<=20){
//				sqlBuff.append(" and cdm_contract.pk_debitorg in '"+queryValueMap.get("borrower")+"'");
//			}else{
//				sqlBuff.append(" and cdm_contract.pk_debitorg in "+queryValueMap.get("borrower"));
//			}
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"cdm_contract.pk_debitorg",
							queryValueMap.get("borrower").split(",")));
		}
		if (queryValueMap.get("pk_fintype") != null) {// 融资类别
			/*sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn("pk_fintype",
							queryValueMap.get("pk_fintype").split(",")));*/
//			if(queryValueMap.get("pk_fintype").length()<=20){
//				sqlBuff.append(" and tgrz_fintype.pk_fintype in '"+queryValueMap.get("pk_fintype")+"'");
//			}else{
//				sqlBuff.append(" and tgrz_fintype.pk_fintype in "+queryValueMap.get("pk_fintype")+"");
//			}
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_fintype.pk_fintype",
							queryValueMap.get("pk_fintype").split(",")));
		}
		if (queryValueMap.get("pk_organization") != null) {// 融资机构
			/*sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn("pk_organization", 
							queryValueMap.get("pk_organization").split(",")));*/
//			if(queryValueMap.get("pk_financeorg").length()<=20){
//				sqlBuff.append(" and tgrz_organization.pk_fintype in '"+queryValueMap.get("pk_organization")+"'");
//			}else{
//				sqlBuff.append(" and tgrz_organization.pk_fintype in "+queryValueMap.get("pk_organization")+"");
//			}
			sqlBuff.append(" and "
					+ SQLUtil.buildSqlForIn(
							"tgrz_organization.name",
							queryValueMap.get("pk_organization").split(",")));
		}
		if (queryValueMap.get("settledate_begin") != null&&queryValueMap.get("settledate_end") != null) {// 贷款结清日
//			sqlBuff.append(" and "
//					+ SQLUtil.buildSqlForIn("cdm_contract_exec.busidate",
//							queryValueMap.get("settledate").split(",")));
			sqlBuff.append(" and (select max(cdm_contract_exec.busidate) from  cdm_contract_exec where ");
			sqlBuff.append("cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
			sqlBuff.append("and cdm_contract_exec.summary = 'HBJ') >= '"+queryValueMap.get("settledate_begin")+"' and (select max(cdm_contract_exec.busidate) from  cdm_contract_exec where ");
			sqlBuff.append("cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
			sqlBuff.append("and cdm_contract_exec.summary = 'HBJ')  <= '"+queryValueMap.get("settledate_end")+"' ");
		}else if(queryValueMap.get("settledate_begin") != null&&queryValueMap.get("settledate_end") == null){
			sqlBuff.append(" and (select max(cdm_contract_exec.busidate) from  cdm_contract_exec where ");
			sqlBuff.append("cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
			sqlBuff.append("and cdm_contract_exec.summary = 'HBJ') >= '"+queryValueMap.get("settledate_begin")+"'  ");
		}else if(queryValueMap.get("settledate_begin") == null&&queryValueMap.get("settledate_end") != null){
			sqlBuff.append(" and  (select max(cdm_contract_exec.busidate) from  cdm_contract_exec where ");
			sqlBuff.append("cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
			sqlBuff.append("and cdm_contract_exec.summary = 'HBJ')<= '"+queryValueMap.get("settledate_end")+"' ");
		}
		
		sqlBuff.append(" and cdm_contract.dr=0");
		sqlBuff.append(" order   by contractcode,loandate,begindate ");
//		sqlBuff.append(""+getGroupBy()+"");
		Collection<FinClosedDetailedVO> coll = (Collection<FinClosedDetailedVO>) getBaseDAO()
			.executeQuery(sqlBuff.toString(),
				new BeanListProcessor(FinClosedDetailedVO.class));

		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinClosedDetailedVO[0]) : null;
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
