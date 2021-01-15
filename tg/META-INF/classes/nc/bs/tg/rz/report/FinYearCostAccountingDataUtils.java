package nc.bs.tg.rz.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.FinYearCostAccountingVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 存量融资成本
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:51:04
 */
public class FinYearCostAccountingDataUtils extends ReportUtils {
	static FinYearCostAccountingDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static FinYearCostAccountingDataUtils getUtils() {
		if (utils == null) {
			utils = new FinYearCostAccountingDataUtils();
		}
		return utils;
	}

	public FinYearCostAccountingDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new FinYearCostAccountingVO()));
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
			List<FinYearCostAccountingVO>vos = getFinYearCostListVOs();
			
			if (vos != null && vos.size() > 0) {
				cmpreportresults = transReportResult(vos);
			
			}
		} catch ( Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
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
	private List<FinYearCostAccountingVO> getFinYearCostListVOs() throws BusinessException   {
		UFDouble totalcost_amount = UFDouble.ZERO_DBL;
		UFDouble  sumloanadd_amount = UFDouble.ZERO_DBL;
		UFDouble  finadviser_amount = UFDouble.ZERO_DBL;
		int count =0;
			StringBuffer sql= new StringBuffer();
			sql.append("select fintypecode,pk_fintype,fintypename,sum(leftrepayamount) fin_balance, ")
			.append(" decode(sum(leftrepayamount), 0, 0, sum(total) / sum(leftrepayamount)) / 100 avecost_amount, ")
			.append("decode(sum(leftrepayamount),0,0,sum(advisertotal) / sum(leftrepayamount)) finadviser_amount ")
			.append("from (select  fintypecode, pk_fintype, fintypename,nvl(payamount,0) - nvl(repayamount,0) as leftrepayamount, ")
			.append("  nvl(pk_htlv,0) pk_htlv,(nvl(payamount,0) -  nvl(repayamount,0)) * nvl(pk_htlv,0) total, ")
			.append("(nvl(payamount,0) -  nvl(repayamount,0)) * nvl(finadviser,0) advisertotal ")
			.append("from (select distinct cdm_contract.pk_contract pk_contract, ")
			.append("cdm_contract.contractcode contractcode,tgrz_fintype.code fintypecode, ")
			.append("tgrz_fintype.pk_fintype pk_fintype,tgrz_fintype.name fintypename, ")
			.append("(select sum(cdm_contract_exec.payamount) from cdm_contract_exec ")
			.append(" where cdm_contract.pk_contract = cdm_contract_exec.pk_contract ")
			.append(" and substr(cdm_contract_exec.busidate,1,4) <= '"+queryValueMap.get("busidate")+"' ")
			.append("and cdm_contract_exec.summary = 'FBJ' and cdm_contract_exec.dr = 0) payamount, ")
			.append("nvl(contexe.repayamount,0)repayamount,cdm_contract.pk_htlv pk_htlv, ")
			.append("nvl(cdm_contract.i_cwgwfl,0) finadviser from cdm_contract ")
			.append("left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx ")
			.append("left  join  (select cdm_contract_exec.pk_contract, sum(nvl(cdm_contract_exec.repayamount,0))repayamount from cdm_contract_exec ")
			.append("where   cdm_contract_exec.summary = 'HBJ'   and cdm_contract_exec.dr = 0  and substr(cdm_contract_exec.busidate,1,4) <= '"+queryValueMap.get("busidate")+"' group  by cdm_contract_exec.pk_contract ")
			.append(" )contexe  on  contexe.pk_contract= cdm_contract.pk_contract  where cdm_contract.pk_rzlx is not null  and cdm_contract.dr = 0")
			.append(" and cdm_contract.contstatus in ('EXECUTING','FINISHED')   ")
			
			.append(" and cdm_contract.pk_contract in "
				+ " (select pk_contract from cdm_contract_exec "
				+ " where cdm_contract_exec.dr = 0"
				+ " and substr(cdm_contract_exec.busidate,1,4) <= '"+queryValueMap.get("busidate")+"' "
				+ "group by pk_contract  "
				+ " having sum(nvl(payamount,0)) > sum(nvl(repayamount,0))) ");
			
			if(queryWhereMap.get("pk_rzlx") != null)
				sql.append(queryWhereMap.get("pk_rzlx"));
			sql.append("   order by contractcode )) group by fintypecode, pk_fintype, fintypename ");
			
			@SuppressWarnings("unchecked")
			List<FinYearCostAccountingVO> volist = (List<FinYearCostAccountingVO>) getBaseDAO()
					.executeQuery(sql.toString(),
							new BeanListProcessor(FinYearCostAccountingVO.class));
			
			 if (volist != null && volist.size() > 0){ 
				 for(FinYearCostAccountingVO vo:volist){
					 sumloanadd_amount = sumloanadd_amount.add(vo.getFin_balance());
				totalcost_amount =totalcost_amount.add(vo.getAvecost_amount() == null ? UFDouble.ZERO_DBL:
					 vo.getAvecost_amount()
						.multiply(vo.getFin_balance()));
				
				finadviser_amount = finadviser_amount.add(vo.getFinadviser_amount() == null ? UFDouble.ZERO_DBL
						: vo.getFinadviser_amount()
						.multiply(vo.getFin_balance()));
				count++;

				 }
				 FinYearCostAccountingVO sumaveVO = new FinYearCostAccountingVO();
					sumaveVO.setFintypename("合计平均数");
					sumaveVO.setFin_balance(sumloanadd_amount);
					
					if(sumloanadd_amount.div(count).equals(UFDouble.ZERO_DBL)){
						sumaveVO.setAvecost_amount(UFDouble.ZERO_DBL);
						sumaveVO.setFinadviser_amount(UFDouble.ZERO_DBL);
					}else{
					sumaveVO.setAvecost_amount(totalcost_amount.div(sumloanadd_amount));
					sumaveVO.setFinadviser_amount(finadviser_amount.div(sumloanadd_amount));
					}
					volist.add(sumaveVO);
			 }
				 
				 
			 
			return volist;
	}
}
