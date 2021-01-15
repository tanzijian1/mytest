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
import nc.vo.tg.rz.report.FinStockCostVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 年度融资成本
 * 
 * 
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:49:28
 */
public class FinStockCostDataUtils extends ReportUtils {
	static FinStockCostDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql


	public static FinStockCostDataUtils getUtils() {
		if (utils == null) {
			utils = new FinStockCostDataUtils();
		}
		return utils;
	}

	public FinStockCostDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new FinStockCostVO()));
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
			List<FinStockCostVO>vos = getFinStockCostListVOs();
			
			if (vos != null && vos.size() > 0) {
				cmpreportresults = transReportResult(vos);
		
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	private List<FinStockCostVO> getFinStockCostListVOs() throws BusinessException   {
		UFDouble totalcost_amount = UFDouble.ZERO_DBL;
		UFDouble  sumloanadd_amount = UFDouble.ZERO_DBL;
		UFDouble  finadviser_amount = UFDouble.ZERO_DBL;
		int count =0;
			StringBuffer sql= new StringBuffer();
			sql.append("select fintypecode,pk_fintype,fintypename,sum(payamount) loanadd_amount, ")
			.append("sum(total)/sum(payamount)/100 fincost_amount,sum(advisertotal)/sum(payamount) finadviser_amount from( ")
			.append("select cdm_contract.pk_contract pk_contract, ")
			.append("cdm_contract.contractcode contractcode, ")
			.append("tgrz_fintype.code fintypecode, ")
			.append("tgrz_fintype.pk_fintype pk_fintype, ")
			.append("tgrz_fintype.name fintypename, ")
			.append("cdm_contract_exec.payamount payamount, ")
			.append("cdm_contract.pk_htlv pk_htlv, ")
			.append("cdm_contract_exec.payamount*cdm_contract.pk_htlv  total, ")
			.append("cdm_contract.i_cwgwfl financialadviserrate, ")
		    .append("cdm_contract_exec.payamount * cdm_contract.i_cwgwfl advisertotal ")
			.append("from cdm_contract left join cdm_contract_exec ")
			.append("on cdm_contract.pk_contract = cdm_contract_exec.pk_contract ")
			.append("left join tgrz_fintype on tgrz_fintype.pk_fintype = cdm_contract.pk_rzlx and tgrz_fintype.dr = 0 ")
			.append("where cdm_contract.pk_rzlx is not null and cdm_contract.pk_rzlx <> '~' ")
			.append("and cdm_contract.dr=0  and (cdm_contract.contstatus = 'EXECUTING' or cdm_contract.contstatus = 'FINISHED' or cdm_contract.contstatus = 'OVERED'   ) and cdm_contract_exec.payamount>0 ")
			.append("and  tgrz_fintype.pk_fintype in(select   tgrz_fintype.pk_fintype from  tgrz_fintype) ");
			if(queryWhereMap.get("pk_rzlx") != null)
				sql.append(queryWhereMap.get("pk_rzlx"));
			if(queryWhereMap.get("busidate") != null)
				sql.append(" and cdm_contract_exec.busidate like '"+queryValueMap.get("busidate")+"-%'");
			sql.append(")group by  fintypecode,pk_fintype,fintypename ");
			
			@SuppressWarnings("unchecked")
			List<FinStockCostVO> volist = (List<FinStockCostVO>) getBaseDAO()
					.executeQuery(sql.toString(),
							new BeanListProcessor(FinStockCostVO.class));
			
			 if (volist != null && volist.size() > 0){ 
				 for(FinStockCostVO vo:volist){
					// if(vo.getFincost_amount()!=null){
			  sumloanadd_amount = sumloanadd_amount.add(vo.getLoanadd_amount());
			//合同利率的加权平均数
			  totalcost_amount = totalcost_amount.add(vo.getFincost_amount() == null ? UFDouble.ZERO_DBL
						: vo.getFincost_amount()
								.multiply(vo.getLoanadd_amount()));
			//财务顾问费利的加权平均数		 	
			finadviser_amount= finadviser_amount.add(vo.getFinadviser_amount()==null?UFDouble.ZERO_DBL
					     :vo.getFinadviser_amount()
					     .multiply(vo.getLoanadd_amount()));
					 	 count++;
					// }
				 }
				 FinStockCostVO sumaveVO = new FinStockCostVO();
					sumaveVO.setFintypename("合计平均数");
					sumaveVO.setLoanadd_amount(sumloanadd_amount);
					sumaveVO.setFincost_amount(totalcost_amount.div(sumloanadd_amount));
					sumaveVO.setFinadviser_amount(finadviser_amount.div(sumloanadd_amount));
					volist.add(sumaveVO);
			 }
				 
				 
			 
			return volist;
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
