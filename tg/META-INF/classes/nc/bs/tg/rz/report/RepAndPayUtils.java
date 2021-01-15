package nc.bs.tg.rz.report;

import java.util.Map;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;

import com.ufida.dataset.IContext;

public class RepAndPayUtils  extends ReportUtils  {

	@Override
	public DataSet getProcessData(IContext context) throws BusinessException {
		// TODO 自动生成的方法存根
		return null;
	}
   public String getRepAndPayField(){
	   StringBuffer fieldSql = new StringBuffer();
	   fieldSql.append("select distinct contractcode code,")//合同编码、
	 //项目主键、项目编码、项目名称
	.append("tgrz_projectdata.pk_projectdata pk_project,tgrz_projectdata.code projectcode,")
	.append("tgrz_projectdata.name projectname, ")  
	 //分期主键、分期名称
	.append("tgrz_projectdata_c.pk_projectdata_c pk_periodization, ")
	.append("tgrz_projectdata_c.periodizationname periodizationname, ")   
	 //项目公司
	.append("tgrz_projectdata.projectcorp  projectcorp, ")  
	 //借款人
	.append("(select org_financeorg.name from org_financeorg  ")  
	.append("where org_financeorg.pk_financeorg = cdm_contract.pk_debitorg)  borrower, ")  
	 //融资类型主键、编码、名称
	.append("tgrz_fintype.pk_fintype pk_fintype,tgrz_fintype.code fintypecode,tgrz_fintype.name fintypename, ")  
	 //融资机构主键、编码、名称
	.append("tgrz_organization.pk_organization pk_organization,tgrz_organization.code organizationcode, ")  
	.append("tgrz_organization.name organizationname, ")  
	 //融资金额
	.append(" contractamount fin_amount,")
/*	//累计放款金额
	.append("(select  sum(execfk.payamount) from  cdm_contract_exec execfk where execfk.pk_contract = cdm_contract.pk_contract")
	.append(" and execfk.summary = 'FBJ'  and substr(execfk.busidate, 0, 4) = 2019   )  loantatol_amount, ")
	//累计还款金额
	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) = 2019   )  contnote, ") //contnote
*/	//融资起始日、融资到期日
	.append("substr(begindate,0,10) begindate,substr(enddate,0,10)enddate, ")  
	 // 还款方式
	.append("returnmode.name repaymenttypename,")
	//合同利率、利息支付方式、利息支付方式名称
	.append("pk_htlv/100 contratio,lxzffs paytype1name,  ")  
	 //财务顾问费率、 财务顾问费用支付方式
	.append("i_cwgwfl adviserratio,cwgwfzffs paytype2name, ");  
	
	   
	   return fieldSql.toString();
	   
   }
   /*
    * 获取年度需要还款计划相关字段
    */
    public String getYearPlanAmount(){
    	StringBuffer planSql= new StringBuffer();
    	//// N月/n+x实际还款金额、
    	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
    	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
    	.append("and substr(execbj.busidate, 0, 4) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
    	//N月/n+x付息金额
    	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
    	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
    	.append("and substr(execlx.busidate, 0, 4) = v1.datatype and execlx.dr='0' )ipmonactualtotal_amount, ")
//    	//累计已还本金
//    	.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
//    	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
//    	.append("and substr(execbj.busidate, 0, 4) <= v1.datatype and execbj.dr='0') repaytotal_amount, ")
    	//计划财务顾问费用金额
    	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
    	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
    	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
    	//实际财务顾问费用金额
    	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
    	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
    	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzxqk.dr='0'  )advmonactualtotal_amount, ")
//    	  // N月/n+x实际还款金额、N月/n+x付息金额
//    	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//    	  //计划财务顾问费用金额、实际财务顾问费用金额
//    	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
    	//用于交叉表的日期数据
    	.append("v1.datatype datatype from cdm_contract ");
    	
		return planSql.toString();
    	
    }
    
    /*
     * 获取年度需要还款计划相关字段 add by tjl 2020-06-03
     */
     public String getYearPlanAmount(Map<String, String> queryValueMap){
     	StringBuffer planSql= new StringBuffer();
     	//// N月/n+x实际还款金额、
     	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
     	.append("and substr(execbj.busidate, 0, 4) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
     	//N月/n+x付息金额
     	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
     	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
     	.append("and substr(execlx.busidate, 0, 4) = v1.datatype and execlx.dr='0' )ipmonactualtotal_amount, ")
//     	//累计已还本金
//     	.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
//     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
//     	.append("and substr(execbj.busidate, 0, 4) <= v1.datatype and execbj.dr='0') repaytotal_amount, ")
//     	.append("(select  sum(execbj.repayamount) from  cdm_contract_exec execbj where execbj.pk_contract = cdm_contract.pk_contract ")  
//     	.append(" and execbj.summary = 'HBJ' and substr(execbj.busidate, 0, 4) <= "+queryValueMap.get("cyear")+"    )  yeartatol_amount, ") //contnote
     	//计划财务顾问费用金额
     	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
     	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
     	//实际财务顾问费用金额
     	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
     	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 4) = v1.datatype and cdm_cwgwfzxqk.dr='0'  )advmonactualtotal_amount, ")
//     	  // N月/n+x实际还款金额、N月/n+x付息金额
//     	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//     	  //计划财务顾问费用金额、实际财务顾问费用金额
//     	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
     	//用于交叉表的日期数据
     	.append("v1.datatype datatype from cdm_contract ");
     	
 		return planSql.toString();
     	
     }
    
    /*
     * 获取月度需要还款计划相关字段
     */
     public String getMonthPlanAmount(){
     	StringBuffer planSql= new StringBuffer();
     	//// N月/n+x实际还款金额、
     	planSql.append(" (select sum(nvl(execbj.repayamount, 0))  from cdm_contract_exec execbj ")
     	.append(" where execbj.pk_contract = cdm_contract.pk_contract and execbj.summary = 'HBJ' ")
     	.append("and substr(execbj.busidate, 0, 7) = v1.datatype and execbj.dr='0')repmonactualtotal_amount, ")
     	//N月/n+x付息金额
     	.append("(select  sum(nvl(execlx.payinterest, 0)) from  cdm_contract_exec execlx ")
     	.append("where execlx.pk_contract = cdm_contract.pk_contract and execlx.summary = 'FLX' ")
     	.append("and substr(execlx.busidate, 0, 7) = v1.datatype and execlx.dr='0')ipmonactualtotal_amount, ")
     	//计划财务顾问费用金额
     	.append(" (select sum(nvl(cdm_cwgwfzfjh.m_zfje, 0)) from cdm_cwgwfzfjh ")
     	.append("where cdm_cwgwfzfjh.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzfjh.d_zfsj, 0, 7) = v1.datatype and cdm_cwgwfzfjh.dr='0' ) advmonplantotal_amount, ")
     	//实际财务顾问费用金额
     	.append("(select sum(nvl(cdm_cwgwfzxqk.m_zfje, 0)) from  cdm_cwgwfzxqk ")
     	.append("where cdm_cwgwfzxqk.pk_contract = cdm_contract.pk_contract ")
     	.append("and substr(cdm_cwgwfzxqk.d_zfsj, 0, 7) = v1.datatype and cdm_cwgwfzxqk.dr='0' )advmonactualtotal_amount, ")
//     	  // N月/n+x实际还款金额、N月/n+x付息金额
//     	planSql.append("sum(nvl(execbj.repayamount,0))  repmonactualtotal_amount,sum(nvl(execlx.payinterest,0)) ipmonactualtotal_amount, ")
//     	  //计划财务顾问费用金额、实际财务顾问费用金额
//     	.append("sum(nvl(cdm_cwgwfzfjh.m_zfje,0)) advmonplantotal_amount,sum(nvl(cdm_cwgwfzxqk.m_zfje,0)) advmonactualtotal_amount, ")
     	//用于交叉表的日期数据
     	.append("v1.datatype datatype from cdm_contract ");
     	
 		return planSql.toString();
     	
     }
   /*
    * 根据还贷计划是现金流还是合同约定来动态获取字段
    */
	public String getMothRepayField(Map<String, String> queryValueMap){
	String 	field="";
		if("合同约定".equals(queryValueMap.get("plan"))){
			field = " cdm_repayplan.preamount repmonplantotal_amount, "; //sum(nvl(
		}else if("现金流".equals(queryValueMap.get("plan"))){
			field = "sum(nvl(cdm_cdjh.m_yjhbj, 0)) repmonplantotal_amount,";
		}
		return field;
	}
	/*
	 * 用于外连接相关表
	 */
	public String getLinkTable(){
	StringBuffer	tableSql= new StringBuffer();
	    //用于连项目资料表
      tableSql.append("left join tgrz_projectdata on cdm_contract.pk_project =tgrz_projectdata.pk_projectdata ")
	  //项目分期表	
	.append("left join tgrz_projectdata_c on tgrz_projectdata_c.pk_projectdata_c=cdm_contract.vdef3  ")
	//项目公司名称参照（财务组织表）
	.append("left join org_financeorg on org_financeorg.pk_financeorg =cdm_contract.pk_xmgs  ")
	 //融资类型表
	.append("left join  tgrz_fintype on   tgrz_fintype.pk_fintype=cdm_contract.pk_rzlx  ")
	//还款方式
	.append(" left join bd_defdoc returnmode  on returnmode.pk_defdoc=cdm_contract.vdef13  ")
	//融资机构表
	.append("left join tgrz_organization on tgrz_organization.pk_organization=cdm_contract.pk_rzjg ");
		
	return tableSql.toString();
		
	}

}
