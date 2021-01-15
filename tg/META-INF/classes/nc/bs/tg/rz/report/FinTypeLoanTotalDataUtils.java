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
 * �������ͷſ�ͳ��
 * 
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����3:46:08
 */
public class FinTypeLoanTotalDataUtils extends ReportUtils {
	static FinTypeLoanTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// ��ѯ�����µ�ֵ
	Map<String, String> queryWhereMap = new HashMap<String, String>();// ��ѯ�����µ�sql

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
		// TODO �Զ����ɵķ������
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
//		sql.append("rzlx.��� as cyear, ");
		sql.append("rzlx.������������ as pk_fintype, ");
		sql.append("rzlx.�������ͱ��� as fintypecode, ");
		sql.append("rzlx.������������ as fintypename, ");
		sql.append("sum(nvl(rzlx.��ȷſ���,0)) as amount ");
			sql.append("from ( ");
		sql.append("select ");
//		sql.append("(case when substr(cdm_contract.enddate, 1, 4) >= '"+queryValueMap.get("cyear")+"' then '"+queryValueMap.get("cyear")+"' else null end) as ���, ");
		sql.append("tgrz_fintype.pk_fintype as ������������, ");
		sql.append("tgrz_fintype.code as �������ͱ���, ");
		sql.append("tgrz_fintype.name as ������������, ");
		sql.append("(case when cdm_contract_exec.busidate >= '"+queryValueMap.get("cyear")+"-01-01' and cdm_contract_exec.busidate <= '"+queryValueMap.get("cyear")+"-12-31' then cdm_contract_exec.payamount else 0 end ) as ��ȷſ��� ");
		sql.append("from cdm_contract ");
		sql.append("left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx ");
		sql.append("left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract ");
		sql.append("where  cdm_contract.pk_rzlx <> '~' ");
		sql.append(" and nvl(cdm_contract.dr,0) = 0 ");
		sql.append(" and nvl(cdm_contract_exec.dr,0) = 0 ");
		sql.append(" and nvl(tgrz_fintype.dr,0) = 0 ");
		sql.append(" and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) ");
		//��������
		if (queryValueMap.get("pk_fintype") != null) {
//			sql.append(" and "
//					+ SQLUtil.buildSqlForIn(
//							"tgrz_fintype.pk_fintype",
//							queryValueMap.get("pk_fintype").split(",")));
			sql.append(" and tgrz_fintype.pk_fintype in "+queryValueMap.get("pk_fintype")+"");
		}
		sql.append(" ) rzlx ");
		sql.append("where nvl(rzlx.��ȷſ���,0)>0");
		sql.append(" group by ");
//		sql.append("rzlx.���, ");
		sql.append(" rzlx.������������, ");
		sql.append(" rzlx.�������ͱ���, ");
		sql.append(" rzlx.������������ ");
		Collection<FinTypeLoanTotalVO> coll = (Collection<FinTypeLoanTotalVO>)
				getBaseDAO()
				.executeQuery(sql.toString(),new BeanListProcessor(FinTypeLoanTotalVO.class));
		return coll != null && coll.size() > 0 ? coll
				.toArray(new FinTypeLoanTotalVO[0]):null;
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
