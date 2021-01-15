package nc.bs.tg.rz.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinancingProgressVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 融资进度表
 * 
 * 
 * @author huangxj
 * @date 2019年9月17日11:06:05
 */
public class FinancingProgressUtils extends ReportUtils {

	static FinancingProgressUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static FinancingProgressUtils getUtils() {
		if (utils == null) {
			utils = new FinancingProgressUtils();
		}
		return utils;
	}

	public FinancingProgressUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinancingProgressVO()));
	}

	public DataSet getProcessData(IContext context) throws BusinessException {
		CmpReportResultVO[] cmpreportresults = null;

		try {
			ConditionVO[] conditionVOs = (ConditionVO[]) context
					.getAttribute(FPConst.KEY_QueryScheme);
			/*if (conditionVOs == null || conditionVOs.length == 0) {
				return generateDateset(cmpreportresults, getKeys());
			}  */
			initQuery(conditionVOs);

			FinancingProgressVO[] vos = getFinancingProgressVOs();
			if (vos != null && vos.length > 0) {
				cmpreportresults = transReportResult(Arrays.asList(vos));
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	/**
	 * 读取月度融资计划VO
	 * 
	 * @return
	 * @throws DAOException
	 */
	private FinancingProgressVO[] getFinancingProgressVOs() throws DAOException {
		StringBuffer sqlBuff = new StringBuffer();
		sqlBuff.append("select tgrz_projectdata.pk_projectdata pk_projectdata,");
		sqlBuff.append("tgrz_projectdata.name proname, ");
		sqlBuff.append("tgrz_projectdata_v.fintype,");
		sqlBuff.append("tgrz_projectdata_v.organization ,");
		sqlBuff.append("tgrz_fintype.name nfintype, ");
		sqlBuff.append("tgrz_projectdata_v.finmny finmny_amount, ");
		sqlBuff.append("tgrz_organization.name norganization, ");
		sqlBuff.append("(case when tgrz_projectdata_v.ismain='y' then '是' else '否' end ) ismain, ");
		sqlBuff.append("tgrz_projectdata_v.bra_nchdate bra_nchdate, ");
		sqlBuff.append("tgrz_projectdata_v.provi_ncedate provi_ncedate, ");
		sqlBuff.append("tgrz_projectdata_v.headquartersdate headquartersdate, ");
		sqlBuff.append("tgrz_projectdata_v.replydate replydate, ");
		sqlBuff.append("tgrz_projectdata_v.costsigndate costsigndate, ");
		sqlBuff.append("tgrz_projectdata_v.remarkschedule remarkschedule, ");
		sqlBuff.append("tgrz_projectdata_b.paymny paymny, ");
		sqlBuff.append("tgrz_projectdata_b.paydate paydate , ");
		sqlBuff.append("(select sum(paymny) from tgrz_projectdata_b b  where b.pk_projectdata = tgrz_projectdata.pk_projectdata) sunmny, ");
		sqlBuff.append("greatest(tgrz_projectdata.nc_datadate4,tgrz_projectdata.nc_datadate5) twocompletetime, ");
		sqlBuff.append("greatest(tgrz_projectdata.nc_datadate4,tgrz_projectdata.nc_datadate5,tgrz_projectdata.nc_datadate6) threecompletetime, ");
		sqlBuff.append("greatest(tgrz_projectdata.nc_datadate4,tgrz_projectdata.nc_datadate5,tgrz_projectdata.nc_datadate6,tgrz_projectdata.nc_datadate7) fourcompletetime ");
		sqlBuff.append("from tgrz_projectdata ");
		sqlBuff.append("left join tgrz_projectdata_v on tgrz_projectdata_v.pk_projectdata=tgrz_projectdata.pk_projectdata 	 ");
		sqlBuff.append("left join tgrz_fintype on tgrz_projectdata_v.fintype=tgrz_fintype.pk_fintype ");
		sqlBuff.append("left join tgrz_organization on tgrz_projectdata_v.organization=tgrz_organization.pk_organization ");
		sqlBuff.append("left join tgrz_projectdata_b on tgrz_projectdata_b.pk_projectdata=tgrz_projectdata.pk_projectdata ");
		sqlBuff.append("where nvl(tgrz_projectdata_v.isdrawescheme,'N') <> 'Y' AND nvl(tgrz_projectdata_v.disable,'N') <> 'Y' and tgrz_projectdata.dr=0 ");

		if (queryValueMap.get("proname") != null) {// 项目名称
			sqlBuff.append(" and tgrz_projectdata.pk_projectdata in "+queryValueMap.get("proname"));
		}

		if (queryValueMap.get("fintype") != null) {// 融资类型
			sqlBuff.append(" and tgrz_projectdata_v.fintype in "+queryValueMap.get("fintype"));
		}

		if (queryValueMap.get("organization") != null) {// 融资机构
			sqlBuff.append(" and tgrz_projectdata_v.organization in "+queryValueMap.get("organization"));
		}

		Collection<FinancingProgressVO> coll = (Collection<FinancingProgressVO>) getBaseDAO()
				.executeQuery(sqlBuff.toString(),
						new BeanListProcessor(FinancingProgressVO.class));

		/*
		 * if(coll.size()>0){ Map<String,Object> pk_projectdata =
		 * getPk_projectdata(coll);
		 * 
		 * for (FinancingProgressVO financingProgressVO : coll) {
		 * if(pk_projectdata
		 * .get(financingProgressVO).equals(financingProgressVO.
		 * getPk_projectdata())){
		 * 
		 * } } }
		 */

		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinancingProgressVO[0]) : null;
	}

	/**
	 * 初始化查询条件信息
	 * 
	 * @param condVOs
	 */
	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		if (condVOs != null && condVOs.length > 0) {
			for (ConditionVO condVO : condVOs) {
				queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}
		}
	}
}
