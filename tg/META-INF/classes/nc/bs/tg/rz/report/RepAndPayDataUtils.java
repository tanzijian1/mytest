package nc.bs.tg.rz.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.RepAndPayVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 还款付息表
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:55:32
 */
public class RepAndPayDataUtils extends RepAndPayUtils {
	static RepAndPayDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql
	public static RepAndPayDataUtils getUtils() {
		if (utils == null) {
			utils = new RepAndPayDataUtils();
		}
		return utils;
	}

	public RepAndPayDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(new RepAndPayVO()));
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
			List<RepAndPayVO>vos = getRepAndPayListVOs();
			
			if (vos != null && vos.size() > 0) 
				cmpreportresults = transReportResult(vos);
		

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}
/*
 * 迭代当前查询年的十二个月份
 */
	private String getThisMonthSql() {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("cyear");
		sql.append(" ( ");
		for(int j=0;j<=4;j++){
			for (int i = 1; i <= 12; i++) {
				sql.append("   select '" + (Integer.parseInt(year)+j ) + "-" + String.format("%02d", i)
						+ "' datatype,'" + (Integer.parseInt(year)+j )+ "年" + String.format("%02d", i) +"月"
						+ "' datatypename from dual ");
				if (i != 12) {
					sql.append(" union all  ");
				}
			}
			if (j != 4){
				sql.append(" union all  ");
			}
	}
			

		sql.append(" ) ");

		return sql.toString();

	}

	private String getNAddYearSql() {
		StringBuffer sql = new StringBuffer();
		String year = queryValueMap.get("cyear");
		sql.append(" ( ");
		for (int i = 0; i <= 4; i++) {
			sql.append("   select '" + (Integer.parseInt(year) + i)
					+ "' datatype,'" + (Integer.parseInt(year) + i)+"年"
					+ "' datatypename from dual ");
			if (i != 4) {
				sql.append(" union all  ");
			}
		}

		sql.append(" ) ");

		return sql.toString();

	}
	private List<RepAndPayVO> getRepAndPayListVOs() throws BusinessException{
		
		StringBuffer sql = new StringBuffer();
		//获取还款付息表所需查询字段
		sql.append(" "+getRepAndPayField() +" ")
		
	//累计放款金额
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	//累计还款金额
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"   )  yeartatol_amount, ") //contnote
	
		//根据月度度还贷计划是现金流还是合同约定来动态获取字段
		.append(" "+getMothRepayField()+" ")
		//增加还款条件
		//.append(" cdm_contract.vdef13 rePaycondition, ")
		// null repyearplantotal_amount,null repyearactualtotal_amount,null ipyearactualtotal_amount,null advyearplantotal_amount, ")
		//.append("null advyearactualtotal_amount
		//获取还款付息相关金额字段
		.append(""+ getMonthPlanAmount()+"")
		//获取相关的连接表
		.append(" "+getLinkTable()+" ")
		//迭代当前查询年的十二个月份
		.append("inner join "+getThisMonthSql() +" v1 on 1=1 ")
		//根据月度度还贷计划是现金流还是合同约定来动态获取表体
		//.append(""+getMonthRepayplanLink() +"")
		//.append("left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract and substr(cdm_repayplan.repaydate,0,7) =datatype  ")
		//根据月份获取还款执行表及财务顾问费的连接表
		//.append(" "+ getMonthTable()+" "  )
		//添加查询条件
		.append(" "+getWhereSql()+ " ");
		//获取分组字段
		sql.append(" "+getGroupBy()+" ");
		
		/*
		 * 联合
		 */
		
		sql.append("union all ")
	     //获取还款付息表所需查询字段
		.append(" "+getRepAndPayField() +"  ")
			//累计放款金额
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	
	//累计还款金额
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ") //contnote
		.append(" "+getYearRepayField()+" ")
		//增加还款条件
		//.append(" cdm_contract.vdef13 rePaycondition, ")
		 //获取还款付息相关金额字段
		.append(""+ getYearPlanAmount()+"")
		 //获取相关的连接表
		.append(" "+getLinkTable()+" ")
		
		.append("inner join "+getNAddYearSql() +" v1 on 1=1 ")
		//.append(""+getYearRepayplanLink() +"")
		//.append("left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract and substr(cdm_repayplan.repaydate,0,4) =datatype  ")
		//.append(" "+ getYearTable()+" "  )
		//添加查询条件
		.append(" "+getWhereSql()+ " ");
		//获取分组字段
		sql.append(" "+getGroupBy()+" ");
		List<RepAndPayVO> volist = (List<RepAndPayVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(RepAndPayVO.class));
		Set<String> keySet = new HashSet<String>();
		if(volist.size()>0){
			for (RepAndPayVO repAndPayVO : volist) {
				keySet.add(repAndPayVO.getCode());
			}
		}
		Map<String,Object> repayMap = null;
		repayMap = getContnote(keySet.toArray(new String[0]));
		//读取融资余额数据
		for(RepAndPayVO vo:volist){
//			UFDouble	fin_amount =UFDouble.ZERO_DBL; 
			vo.setContnote(repayMap.get(vo.getCode())==null?null:repayMap.get(vo.getCode()).toString());
//			UFDouble loantatol = vo.getLoantatol_amount();//累计放款
//			UFDouble yeartatol = vo.getYeartatol_amount();//累计还款
//			vo.setContnote(contnote);
//			fin_amount = vo.getLoantatol_amount() != null ? new UFDouble(
//					vo.getLoantatol_amount()).sub(vo
//					.getContnote() != null ? new UFDouble(vo.getContnote())
//					: UFDouble.ZERO_DBL) : UFDouble.ZERO_DBL;
//					vo.setContnote(fin_amount.setScale(2, UFDouble.ROUND_CEILING).toString());		
		}
		return volist;
		
	}
	
	public Map<String, Object> getContnote(String[] keys) throws BusinessException{
		Map<String,Object> infoMap = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select cdm_contract.contractcode contractcode,sum(cdm_contract_exec.payamount) payamount,repay.repayamount,sum(nvl(cdm_contract_exec.payamount,0)) - nvl(repay.repayamount,0) contnote from cdm_contract ");
		sql.append(" left join cdm_contract_exec on cdm_contract_exec.pk_contract = cdm_contract.pk_contract and cdm_contract_exec.dr = 0 ");
		sql.append(" left join ( ");
		sql.append(" select sum(NVL(cdm_repayrcpt_b.repayamount, 0)) repayamount,cdm_repayrcpt.pk_contract from cdm_repayrcpt ");
		sql.append(" left join  cdm_repayrcpt_b on cdm_repayrcpt.pk_repayrcpt = cdm_repayrcpt_b.pk_repayrcpt and cdm_repayrcpt_b.dr = 0  ");
		sql.append(" where cdm_repayrcpt.vbillstatus = 1 ");
		sql.append(" group by ");
		sql.append(" cdm_repayrcpt.pk_contract ");
		sql.append(" ) repay ");
		sql.append(" on cdm_contract.pk_contract = repay.pk_contract ");
		sql.append(" and  "+SQLUtil.buildSqlForIn(" cdm_contract.contractcode", keys ));
		sql.append(" group by ");
		sql.append(" cdm_contract.contractcode, ");
		sql.append(" repay.repayamount ");
		List<Map<String,Object>> listvo = (List<Map<String, Object>>) getBaseDAO()
				.executeQuery(
						sql.toString(),new MapListProcessor());
		if(listvo.size()>0){
			for (Map<String, Object> map : listvo) {
				infoMap.put(map.get("contractcode").toString(), map.get("contnote"));
			}
		}
		return infoMap;
	}
	
	/**
	 * 根据年度还贷计划是现金流还是合同约定来动态获取字段
	 * 
	 * 
	 */
	private String getYearRepayField(){
		StringBuffer 	field=new StringBuffer();
		if("合同约定".equals(queryValueMap.get("plan"))){
			//N月/n+x年计划还款金额
			field.append("(select   sum(nvl(cdm_repayplan.preamount, 0)) from ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract   ")
			.append("and substr(cdm_repayplan.repaydate, 0, 4) = v1.datatype  and cdm_repayplan.dr='0' ) repmonplantotal_amount, ")
			//N月/n+x计划付息金额
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0)) from  ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_repayplan.repaydate, 0, 4) = v1.datatype and cdm_repayplan.dr='0' ) ipmonplantotal_amount, ");
			/*field = " sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal_amount,"//N月/n+x年计划还款金额
				+ "sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal_amount, "; //N月/n+x计划付息金额
*/		}else if("现金流".equals(queryValueMap.get("plan"))){
			//N月/n+x年计划还款金额
			field.append("(select  sum(nvl(cdm_cdjh.m_yjhbj, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' ) repmonplantotal_amount,")
			//N月/n+x计划付息金额
			.append("(select  sum(nvl(cdm_cdjh.m_yjhlx, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
			.append("and substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal_amount, ");
			/*field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,"//N月/n+x年计划还款金额
					+ "sum(nvl(cdm_cdjh.m_yjhlx, 0)) ipmonplantotal_amount, ";//N月/n+x计划付息金额
*/		}
		return field.toString();
	}
	/**
	 * 根据月度度还贷计划是现金流还是合同约定来动态获取字段
	 * 
	 * 
	 */
	private String getMothRepayField(){//计划还本金，计划还利息
	StringBuffer 	field=new StringBuffer();
		if("合同约定".equals(queryValueMap.get("plan"))){
			//N月/n+x年计划还款金额
			field.append("(select   sum(nvl(cdm_repayplan.preamount, 0)) from ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract   ")
			.append("and substr(cdm_repayplan.repaydate, 0, 7) = v1.datatype  and cdm_repayplan.dr='0' ) repmonplantotal_amount, ")
			//N月/n+x计划付息金额
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0)) from  ")
			.append("cdm_repayplan where cdm_repayplan.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_repayplan.repaydate, 0, 7) = v1.datatype and cdm_repayplan.dr='0'  ) ipmonplantotal_amount, ");
			//field = " sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal_amount,"//N月/n+x年计划还款金额
				//+ "sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal_amount, "; //N月/n+x计划付息金额
		}else if("现金流".equals(queryValueMap.get("plan"))){
			field.append("(select  sum(nvl(cdm_cdjh.m_yjhbj, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append("and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' ) repmonplantotal_amount,")
			//N月/n+x计划付息金额
			.append("(select  sum(nvl(cdm_cdjh.m_yjhlx, 0)) from  cdm_cdjh  ")
			.append("where  cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
			.append("and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal_amount, ");
			//field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,"//N月/n+x年计划还款金额
					//+ "sum(nvl(cdm_cdjh.m_yjhlx, 0)) ipmonplantotal_amount, ";//N月/n+x计划付息金额
		}
		return field.toString();
	}
	
	/*
	 * 根据年份获取还款执行表及财务顾问费的连接表	
	 */
	private String getYearTable(){
		StringBuffer repAndPaySql=new StringBuffer();
		//银行贷款合同明细单据的的执行表表体用于查询还本金
		repAndPaySql.append("left join  cdm_contract_exec  execbj on execbj.pk_contract = cdm_contract.pk_contract ")
		.append("and  execbj.summary='HBJ' and substr(execbj.busidate,0,4) =  v1.datatype ")
		 //银行贷款合同明细单据的的执行表表体用于查询已还付款金额
/*		.append("left join  cdm_contract_exec  execfk on execfk.pk_contract = cdm_contract.pk_contract and execfk.summary='FBJ' ")	
		.append(" and substr(execfk.busidate,0,4) = "+queryValueMap.get("cyear")+"  ")	*/
		//银行贷款合同明细单据的的执行表表体用于查询已还利息
		.append("left join  cdm_contract_exec  execlx on execlx.pk_contract = cdm_contract.pk_contract and execlx.summary='FLX' ")	
		.append("and substr(execlx.busidate,0,4) = v1.datatype ")	
		 //计划财务顾问费用金额
		.append("left join cdm_cwgwfzfjh on    cdm_cwgwfzfjh.pk_contract= cdm_contract.pk_contract ")	
		.append("and  substr(cdm_cwgwfzfjh.d_zfsj,0,4) = v1.datatype  ")	
		 //实际财务顾问费用金额
		.append("left join cdm_cwgwfzxqk on    cdm_cwgwfzxqk.pk_contract= cdm_contract.pk_contract ")
		.append("and substr(cdm_cwgwfzxqk.d_zfsj,0,4)=  v1.datatype  ");
		return repAndPaySql.toString();
		
	}
	/*
	 * 获取分组字段
	 */
	public String getGroupBy(){
		StringBuffer groupSql =new StringBuffer();
		 	//
		 groupSql.append("group by contractcode,tgrz_projectdata.pk_projectdata ,tgrz_projectdata.code,tgrz_projectdata.name, ")
		.append("tgrz_projectdata_c.pk_projectdata_c,tgrz_projectdata_c.periodizationname,projectcorp,pk_debitorg, ")
		.append("tgrz_fintype.pk_fintype,tgrz_fintype.code,tgrz_fintype.name ,cdm_contract.vdef13, ")
		.append("tgrz_organization.code, tgrz_organization.pk_organization,tgrz_organization.name, ")
		.append("contractamount, begindate,enddate,returnmode,pk_htlv,lxzffs, cdm_contract.pk_contract,returnmode.name,cwgwfzffs,i_cwgwfl,v1.datatype ");//execfk.payamount,
		//.append("execbj.repayamount,execlx.payinterest,cdm_cwgwfzfjh.m_zfje,cdm_cwgwfzxqk.m_zfje,v1.datatype ");
		/* if(StringUtils.isNotBlank(queryValueMap.get("plan")))
			 groupSql.append(",cdm_repayplan.preamount ");*/
		return groupSql.toString();
		
		
	}
	/*
	 * 根据界面查询模板带过来的查询条件进行组织sql语句
	 */
	public  String getWhereSql(){
		StringBuffer whereSql= new StringBuffer();
	 whereSql.append("where '1'='1' and cdm_contract.dr=0 and  cdm_contract.contstatus='EXECUTING' ");
	 	//添加项目资料条件
	 if(queryValueMap.get("pk_projectdata") != null){
			whereSql.append(" "+queryWhereMap.get("pk_projectdata")+"" );
		}
	 	//添加项目分期
	 if(queryValueMap.get("pk_projectdata_c") != null){
			whereSql.append(" "+queryWhereMap.get("pk_projectdata_c")+"" );
		}
	 	//项目公司名称
	 if(queryValueMap.get("orgname") != null){
			whereSql.append(" "+queryWhereMap.get("orgname")+"" );
		}
	 	//添加融资类型
	 if(queryValueMap.get("pk_fintype") != null){
			whereSql.append(" "+queryWhereMap.get("pk_fintype")+"" );
		}
	 //借款人
	 if(queryValueMap.get("borrower") != null){
			whereSql.append(" "+queryWhereMap.get("borrower")+"" );
		}
	 	//添加机构
	 if(queryValueMap.get("pk_organization") != null){
			whereSql.append(" "+queryWhereMap.get("pk_organization")+"" );
		}
		return whereSql.toString();
	}
	/**
	 * 初始化查询条件信息
	 * 
	 * @param condVOs
	 */
	private void initQuery(ConditionVO[] condVOs) {
		queryValueMap.clear();
		queryWhereMap.clear();
		//List<String> filed =	Arrays.asList(new String []{"pk_projectdata","pk_projectdata_c"});
		for (ConditionVO condVO : condVOs) {
			queryValueMap.put(condVO.getFieldCode(), condVO.getValue());
			if(condVO.getSQLStr()==null){
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}else if("pk_projectdata".equals(condVO.getFieldCode())){
				//项目名称
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace(condVO.getFieldCode(), "tgrz_projectdata."+condVO.getFieldCode()+""));
			}else if("pk_projectdata_c".equals(condVO.getFieldCode())){
				//项目分期
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_projectdata_c", "tgrz_projectdata_c.pk_projectdata_c"));
			}else if("orgname".equals(condVO.getFieldCode())){
				//项目分期名称
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("orgname", "cdm_contract.pk_xmgs"));
			}else if("borrower".equals(condVO.getFieldCode())){
				//借款人
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("borrower", "cdm_contract.pk_debitorg"));
			}else if("pk_fintype".equals(condVO.getFieldCode())){
				//融资类型
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_fintype", "tgrz_fintype.pk_fintype"));
			}else if("pk_organization".equals(condVO.getFieldCode())){
				//融资机构
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr().replace("pk_organization", "tgrz_organization.pk_organization"));
			}else{
				queryWhereMap.put(condVO.getFieldCode(), condVO.getSQLStr());
			}
			
			
		}
	}
}
