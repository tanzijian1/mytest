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
import nc.vo.tg.rz.report.FinTypeLoanTotalVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 融资类型放款统计
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:46:08
 */
public class FinTypeLoanTotalDataUtils extends ReportUtils {
	static FinTypeLoanTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static FinTypeLoanTotalDataUtils getUtils() {
		if (utils == null) {
			utils = new FinTypeLoanTotalDataUtils();
		}
		return utils;
	}

	public FinTypeLoanTotalDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinTypeLoanTotalVO()));
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
			FinTypeLoanTotalVO [] vos = getFinTypeLoanTotalVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}
			
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private FinTypeLoanTotalVO[] getFinTypeLoanTotalVOs() throws BusinessException{
		// TODO 自动生成的方法存根
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
//		sql.append("rzlx.年度 as cyear, ");
		sql.append("rzlx.融资类型主键 as pk_fintype, ");
		sql.append("rzlx.融资类型编码 as fintypecode, ");
		sql.append("rzlx.融资类型名称 as fintypename, ");
		sql.append("sum(nvl(rzlx.年度放款金额,0)) as amount ");
			sql.append("from ( ");
		sql.append("select ");
//		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as 年度, ");
		sql.append("tgrz_fintype.pk_fintype as 融资类型主键, ");
		sql.append("tgrz_fintype.code as 融资类型编码, ");
		sql.append("tgrz_fintype.name as 融资类型名称, ");
		sql.append("(case when cdm_contract_exec.busidate >= '"+queryValueMap.get("cyear")+"-01-01' and cdm_contract_exec.busidate <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else 0 end ) as 年度放款金额 ");
		sql.append("from cdm_contract ");
		sql.append("left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx ");
		sql.append("left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("where  cdm_contract.pk_rzlx <> '~' ");
		sql.append(" and nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" and nvl(cdm_contract_exec.dr,0) = 0 ");
		sql.append(" and nvl(tgrz_fintype.dr,0) = 0 ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		//融资类型
		if (queryValueMap.get("pk_fintype") != null) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							"tgrz_fintype.pk_fintype",
//							queryValueMap.get("pk_fintype").split(",")));
			sql.append(" and tgrz_fintype.pk_fintype in "+queryValueMap.get("pk_fintype")+"");
		}
		sql.append(" ) rzlx ");
		sql.append("where nvl(rzlx.年度放款金额,0)>0");
		sql.append(" group by ");
//		sql.append("rzlx.年度, ");
		sql.append(" rzlx.融资类型主键, ");
		sql.append(" rzlx.融资类型编码, ");
		sql.append(" rzlx.融资类型名称 ");
		Collection<FinTypeLoanTotalVO> coll = (Collection<FinTypeLoanTotalVO>)
				getBaseDAO()
				.executeQuery(sql.toString(),new BeanListProcessor(FinTypeLoanTotalVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinTypeLoanTotalVO[0]):null;
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
