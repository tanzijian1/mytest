package nc.bs.tg.rz.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.pub.smart.data.DataSet;
import nc.vo.cmp.report.CmpReportResultVO;
import nc.vo.pub.BeanHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.tg.rz.report.RepAndPayTotalVO;
import nc.vo.tgfp.pub.common.FPConst;

import com.ufida.dataset.IContext;

/**
 * 还款付息汇总表
 * 
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 下午3:55:18
 */
public class RepAndPayTotalDataUtils extends RepAndPayUtils {
	static RepAndPayTotalDataUtils utils;
	Map<String, String> queryValueMap = new HashMap<String, String>();// 查询条件下的值
	Map<String, String> queryWhereMap = new HashMap<String, String>();// 查询条件下的sql

	public static RepAndPayTotalDataUtils getUtils() {
		if (utils == null) {
			utils = new RepAndPayTotalDataUtils();
		}
		return utils;
	}

	public RepAndPayTotalDataUtils() {
		setKeys(BeanHelper.getInstance().getPropertiesAry(
				new RepAndPayTotalVO()));
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
			
			List<RepAndPayTotalVO>   vos = getRepAndPayListTotalVOs();
			
			if (vos != null && vos.size() > 0) 
				cmpreportresults = transReportResult(vos);
		

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return generateDateset(cmpreportresults, getKeys());
	}

	
	private  List<RepAndPayTotalVO>getRepAndPayListTotalVOs() throws DAOException{
		
		StringBuffer sql = new StringBuffer();
		//获取还款付息表所需查询字段
		sql.append(" "+getRepAndPayField() +" ")
		//累计放款金额
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	//累计还款金额
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
    .append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ")
		//根据还贷计划是现金流还是合同约定来动态获取字段
		.append(""+getMonthRepayField()+"  ")
		//.append(" "+getRepayField()+" ")
		//获取需要还款计划相关字段
		.append(""+getMonthPlanAmount()+"")
		//.append(" "+getPlanAmount()+" ")
		//获取相关的连接表
		.append(" "+getLinkTable()+" ")
		.append("inner join "+getThisMonthSql() +" v1 on 1=1 ")
		//根据还贷计划是现金流还是合同约定来动态获取关联表
	//	.append(""+getRepayplanLink() +"")
		
		//.append("left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract and substr(cdm_repayplan.repaydate,0,7) =datatype  ")
		
		//.append(" "+getMonthTotalTable()+" ")
		//获取where语句sql
		.append(" "+getWhereSql()+" ");
		sql.append(" "+getGourpBy()+" ");
		
		sql.append("union all ")
		 //获取还款付息表所需查询字段
		.append(""+getRepAndPayField()+" ")
		//累计放款金额
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  loantatol_amount, ")
	//累计还款金额
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
    .append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ")
		 //根据还贷计划是现金流还是合同约定来动态获取字段
		.append(""+getYearRepayField()+"  ")
		//.append(" "+getRepayField()+"  ")
		 //获取需要还款计划相关字段
		.append(""+getYearPlanAmount(queryValueMap)+"")
		//.append(" "+getPlanAmount()+" ")
		 //获取相关的连接表
		.append(" "+getLinkTable()+" ")
		.append("inner join "+getNAddYearSql() +" v1 on 1=1 ")
		//.append(""+getRepayplanLink() +"")
		//.append(" "+getYearTotalTable()+" ")
		//获取where语句sql
		.append(" "+getWhereSql()+" ");
		
		sql.append(" "+getGourpBy()+" ");	
		List<RepAndPayTotalVO> volist = (List<RepAndPayTotalVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(RepAndPayTotalVO.class));
		
		
		return volist;
		
		
		
	}
	
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
	/**
	 * 根据还贷计划是现金流还是合同约定来动态获取sql
	 * 
	 * 
	 */
	private  String getRepayplanLink(){
		StringBuffer  sql =new StringBuffer() ;
		if("合同约定".equals(queryValueMap.get("plan"))){
		sql.append( "left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract ")
		.append("and substr(cdm_repayplan.repaydate,0,7) =v1.datatype ");
		//.append("left join  cdm_contract_exec  execlx on execlx.pk_contract = cdm_contract.pk_contract ")
		//.append("and execlx.summary='FLX'  and substr(execlx.busidate,0,7) = datatype " );
		}else if("现金流".equals(queryValueMap.get("plan"))){
			sql.append("left join cdm_cdjh on cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
					.append(" and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype ")	;
		}else if("('合同约定','现金流')".equals(queryValueMap.get("plan"))){
			sql.append(" left join cdm_repayplan on cdm_repayplan.pk_contract = cdm_contract.pk_contract ")
					.append(" and substr(cdm_repayplan.repaydate,0,7) =v1.datatype ") 
					.append("left join cdm_cdjh on cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
					.append("and substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype ");
		}
		return sql.toString();
	}
	
	/**
	 * 根据月份还贷计划是现金流还是合同约定来动态获取字段
	 * 
	 * 
	 */
	private String getMonthRepayField(){
	StringBuffer field = new  StringBuffer();
		if("合同约定".equals(queryValueMap.get("plan"))){
		//N月计划还款金额(合同约定)
			field.append("(select sum(nvl(cdm_repayplan.preamount, 0))  from cdm_repayplan where  cdm_repayplan.pk_contract = cdm_contract.pk_contract  ")
			.append(" and substr(cdm_repayplan.repaydate,0,7) =v1.datatype and cdm_repayplan.dr='0' )repmonplantotal1_amount,   ")
		// N月付息金额(合同约定) 
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract  ")
			.append(" and substr(cdm_repayplan.repaydate,0,7) =v1.datatype and cdm_repayplan.dr='0')ipmonplantotal1_amount,  ");
		//	field.append("sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal1_amount, ")// N月计划还款金额(合同约定)
			//		.append("sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal1_amount, ");// N月付息金额(合同约定) 
		}else if("现金流".equals(queryValueMap.get("plan"))){
		// N月计划还款金额(现金流)
		field.append("(select sum(nvl(cdm_cdjh.m_yjhbj, 0)) from cdm_cdjh where cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
		.append("  and  substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' )repmonplantotal2_amount,  ")
		// N月付息金额(现金流)
		.append("(select sum(nvl(cdm_cdjh.m_yjhlx, 0))  from cdm_cdjh  where   cdm_cdjh.pk_contract=cdm_contract.pk_contract   ")
		.append(" and  substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal2_amount,  ");
			//field.append("sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal2_amount, ")// N月计划还款金额(现金流)
			//.append(" sum(nvl(cdm_cdjh.m_yjhlx, 0))  ipmonplantotal2_amount,  ");// N月付息金额(现金流)
		}else {//if("('合同约定','现金流')".equals(queryValueMap.get("plan"))) 
			//N月计划还款金额(合同约定)
			field.append("(select sum(nvl(cdm_repayplan.preamount, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract  ")
			.append("and substr(cdm_repayplan.repaydate,0,7) =v1.datatype and cdm_repayplan.dr='0' ) repmonplantotal1_amount,   ")
			// N月计划还款金额(现金流)
			.append("(select sum(nvl(cdm_cdjh.m_yjhbj, 0)) from cdm_cdjh  where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append(" and  substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0' )repmonplantotal2_amount,  ")
			// N月付息金额(合同约定) 
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0))  from cdm_repayplan where  cdm_repayplan.pk_contract = cdm_contract.pk_contract  ")
			.append(" and substr(cdm_repayplan.repaydate,0,7) =v1.datatype and cdm_repayplan.dr='0' ) ipmonplantotal1_amount,  ")
			// N月付息金额(现金流)
			.append("(select sum(nvl(cdm_cdjh.m_yjhlx, 0))  from cdm_cdjh where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
			.append(" and  substr(cdm_cdjh.d_hkrq,0,7) =v1.datatype and cdm_cdjh.dr='0') ipmonplantotal2_amount,  ");
			//field.append(" sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal1_amount, ")// N月计划还款金额(合同约定)
			//.append(" sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal2_amount, ")// N月计划还款金额(现金流)
			//.append("sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal1_amount, ")// N月付息金额(合同约定)
			//.append(" sum(nvl(cdm_cdjh.m_yjhlx, 0))  ipmonplantotal2_amount,  ");// N月付息金额(现金流)
		}
		return field.toString();
	}
	/**
	 * 根据年份还贷计划是现金流还是合同约定来动态获取字段
	 * 
	 * 
	 */
	private String getYearRepayField(){
	StringBuffer field = new  StringBuffer();
		if("合同约定".equals(queryValueMap.get("plan"))){
		//N月计划还款金额(合同约定)
			field.append("(select sum(nvl(cdm_repayplan.preamount, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract  ")
			.append(" and substr(cdm_repayplan.repaydate,0,4) =v1.datatype and cdm_repayplan.dr='0') repmonplantotal1_amount,   ")
		// N月付息金额(合同约定) 
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract   ")
			.append(" and substr(cdm_repayplan.repaydate,0,4) =v1.datatype and cdm_repayplan.dr='0' )ipmonplantotal1_amount,  ");
		//	field.append("sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal1_amount, ")// N月计划还款金额(合同约定)
			//		.append("sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal1_amount, ");// N月付息金额(合同约定) 
		}else if("现金流".equals(queryValueMap.get("plan"))){
		// N月计划还款金额(现金流)
		field.append("(select sum(nvl(cdm_cdjh.m_yjhbj, 0)) from cdm_cdjh where  cdm_cdjh.pk_contract=cdm_contract.pk_contract  ")
		.append(" and  substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0') repmonplantotal2_amount,  ")
		// N月付息金额(现金流)
		.append("(select sum(nvl(cdm_cdjh.m_yjhlx, 0))  from cdm_cdjh  where cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
		.append("and  substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0')ipmonplantotal2_amount,  ");
			//field.append("sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal2_amount, ")// N月计划还款金额(现金流)
			//.append(" sum(nvl(cdm_cdjh.m_yjhlx, 0))  ipmonplantotal2_amount,  ");// N月付息金额(现金流)
		}else {//if("('合同约定','现金流')".equals(queryValueMap.get("plan"))) 
			//N月计划还款金额(合同约定)
			field.append("(select sum(nvl(cdm_repayplan.preamount, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract ")
			.append(" and substr(cdm_repayplan.repaydate,0,4) =v1.datatype and cdm_repayplan.dr='0' ) repmonplantotal1_amount,   ")
			// N月计划还款金额(现金流)
			.append("(select sum(nvl(cdm_cdjh.m_yjhbj, 0)) from cdm_cdjh  where  cdm_cdjh.pk_contract=cdm_contract.pk_contract ")
			.append(" and  substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' )repmonplantotal2_amount,  ")
			// N月付息金额(合同约定) 
			.append("(select sum(nvl(cdm_repayplan.preinterest, 0))  from cdm_repayplan where cdm_repayplan.pk_contract = cdm_contract.pk_contract ")
			.append(" and substr(cdm_repayplan.repaydate,0,4) =v1.datatype and cdm_repayplan.dr='0') ipmonplantotal1_amount,  ")
			// N月付息金额(现金流)
			.append("(select sum(nvl(cdm_cdjh.m_yjhlx, 0))  from cdm_cdjh where  cdm_cdjh.pk_contract=cdm_contract.pk_contract   ")
			.append(" and  substr(cdm_cdjh.d_hkrq,0,4) =v1.datatype and cdm_cdjh.dr='0' ) ipmonplantotal2_amount,  ");
			//field.append(" sum(nvl(cdm_repayplan.preamount, 0)) repmonplantotal1_amount, ")// N月计划还款金额(合同约定)
			//.append(" sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal2_amount, ")// N月计划还款金额(现金流)
			//.append("sum(nvl(cdm_repayplan.preinterest, 0)) ipmonplantotal1_amount, ")// N月付息金额(合同约定)
			//.append(" sum(nvl(cdm_cdjh.m_yjhlx, 0))  ipmonplantotal2_amount,  ");// N月付息金额(现金流)
		}
		return field.toString();
	}
	/*
	 * 根据迭代的月份与执行明细表及财务顾问费表
	 */
	public String getMonthTotalTable(){
		StringBuffer yearTotalSql= new StringBuffer();
		//单据银行贷款合同明细的表体合同执行明细取已还本金
		yearTotalSql.append("left join  cdm_contract_exec  execbj on execbj.pk_contract = cdm_contract.pk_contract  ")
		.append("and  execbj.summary='HBJ' and substr(execbj.busidate,0,7) =  v1.datatype ")
		//单据银行贷款合同明细的表体合同执行明细取付本金
		/*.append("left join  cdm_contract_exec  execfk on execfk.pk_contract = cdm_contract.pk_contract  ")
		.append("and execfk.summary='FBJ' and substr(execfk.busidate,0,4) = "+queryValueMap.get("cyear")+"  ")*/
		//计划财务顾问费用金额
		.append("left join cdm_cwgwfzfjh on    cdm_cwgwfzfjh.pk_contract= cdm_contract.pk_contract ")
		.append(" and  substr(cdm_cwgwfzfjh.d_zfsj,0,7) = v1.datatype ")
		//实际财务顾问费用金额
		.append("left join cdm_cwgwfzxqk on    cdm_cwgwfzxqk.pk_contract= cdm_contract.pk_contract ")
		.append("and substr(cdm_cwgwfzxqk.d_zfsj,0,7)=  v1.datatype ");
		return yearTotalSql.toString();
		
	}
	/*
	 * 根据迭代的月份与执行明细表及财务顾问费表
	 */
		public String getYearTotalTable(){
		StringBuffer yearTotalSql= new StringBuffer();
		//单据银行贷款合同明细的表体合同执行明细取已还本金
		yearTotalSql.append("left join  cdm_contract_exec  execbj on execbj.pk_contract = cdm_contract.pk_contract ")
		.append("and  execbj.summary='HBJ' and substr(execbj.busidate,0,4) =  v1.datatype ")
		//单据银行贷款合同明细的表体合同执行明细取付本金
		/*.append("left join  cdm_contract_exec  execfk on execfk.pk_contract = cdm_contract.pk_contract  ")
		.append("and execfk.summary='FBJ' and substr(execfk.busidate,0,4) = "+queryValueMap.get("cyear")+"   ")*/
		//计划财务顾问费用金额
		.append("left join cdm_cwgwfzfjh on    cdm_cwgwfzfjh.pk_contract= cdm_contract.pk_contract ")
		.append("and  substr(cdm_cwgwfzfjh.d_zfsj,0,4) = v1.datatype  ")
		//实际财务顾问费用金额
		.append("left join cdm_cwgwfzxqk on    cdm_cwgwfzxqk.pk_contract= cdm_contract.pk_contract ")
		.append("and substr(cdm_cwgwfzxqk.d_zfsj,0,4)=  v1.datatype ");
		return yearTotalSql.toString();
		
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
		 	//添加机构
		 if(queryValueMap.get("pk_organization") != null){
				whereSql.append(" "+queryWhereMap.get("pk_organization")+"" );
			}
		//借款人
		 if(queryValueMap.get("borrower") != null){
				whereSql.append(" "+queryWhereMap.get("borrower")+"" );
			}
			return whereSql.toString();
		}	
		
	/*
	 * 获取分组字段sql
	*/
	public String getGourpBy(){
	StringBuffer	groupSql=new  StringBuffer();
	//
	groupSql.append("group by contractcode,tgrz_projectdata.pk_projectdata ,tgrz_projectdata.code,tgrz_projectdata.name, ")
	.append("tgrz_projectdata_c.pk_projectdata_c,tgrz_projectdata_c.periodizationname,projectcorp,pk_debitorg, ")
	.append("tgrz_fintype.pk_fintype,tgrz_fintype.code,tgrz_fintype.name , ")
	.append("tgrz_organization.code, tgrz_organization.pk_organization,tgrz_organization.name,returnmode.name,  ")
	.append("contractamount, begindate,enddate,returnmode,pk_htlv,lxzffs,cwgwfzffs,i_cwgwfl, cdm_contract.pk_contract,v1.datatype");//execfk.payamount,
	//.append("execbj.repayamount,cdm_cwgwfzfjh.m_zfje,cdm_cwgwfzxqk.m_zfje,v1.datatype ");
	return groupSql.toString();
	}
		/*
	    * 获取需要还款计划相关字段
	    */
	 public String getPlanAmount(){
		 StringBuffer planSql= new StringBuffer();
		 planSql.append(" ")
		 .append("execbj.repayamount  repmonactualtotal_amount,cdm_cwgwfzfjh.m_zfje advmonplantotal_amount, ")
		 .append(" cdm_cwgwfzxqk.m_zfje advmonactualtotal_amount,v1.datatype datatype from cdm_contract ");
		
		 return planSql.toString();
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
