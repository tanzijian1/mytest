package nc.impl.pub.ace;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.itf.tg.outside.IRepaymentRefContractService;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

public class RepaymentRefContractServiceImpl implements IRepaymentRefContractService{
	
	BaseDAO dao = null;
	@Override
	public String getContractSqlmain(String pk_org) {
		
		String sql = "select pk_org from cdm_contract where pk_org='"
				+ pk_org + "' and nvl(dr,0)=0 and pk_billtypecode = '36FB' ";
		StringBuffer sqltotal = new StringBuffer();
		try {
			//判断是否为主借款人
			String headorg = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
			//判断是否为联合借款人
			String sqlb = "select pk_org from cdm_contract where pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 and pk_billtypecode = '36FB'";
			List<String> bodyorg = (List<String>) getBaseDAO().executeQuery(sqlb,new ColumnListProcessor());
			if(StringUtils.isNotBlank(headorg)){
				sqltotal.append(" and pk_org = '" + headorg + "'");
			}
		} 
		catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return StringUtils.isBlank(sqltotal.toString())?"  ":sqltotal.toString();
	}
	
	@Override
	public String getContractSql(String pk_org) {
		
		StringBuffer sqltotal = new StringBuffer();
		try {
//			//判断是否为主借款人
//			String headorg = (String) getQueryBS().executeQuery(sql,new ColumnProcessor());
//			//判断是否为联合借款人
//			String sqlb = "select pk_org from cdm_contract where pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 and pk_billtypecode = '36FB'";
//			List<String> bodyorg = (List<String>) getQueryBS().executeQuery(sqlb,new ColumnListProcessor());
//			if(StringUtils.isNotBlank(headorg)&&(bodyorg!=null&&bodyorg.size()>0)){
//				int i = 0;
//				sqltotal.append(" and pk_org = '" + headorg + "'");
//				for (String pk_jkgs : bodyorg) {
//					if(i==bodyorg.size()-1){
//						sqltotal.append(" or pk_org = '"+pk_jkgs+"' ");
//					}else if(i<bodyorg.size()-1){
//						sqltotal.append(" or pk_org = '"+pk_jkgs+"'  ");
//					}
//					i++;
//				}
//				sqltotal.append("and nvl(dr,0)=0 and pk_billtypecode = '36FB' and pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) ");
//			}else if(StringUtils.isNotBlank(headorg)&&(bodyorg==null||bodyorg.size()<=0)){
//				sqltotal.append(" and pk_org = '" + headorg + "'");
//			}else if(StringUtils.isBlank(headorg)&&(bodyorg!=null&&bodyorg.size()>0)){
//				int i = 0;
//				for (String pk_jkgs : bodyorg) {
//					if(i==bodyorg.size()-1&&bodyorg.size()>1){
//						sqltotal.append(" or pk_org = '"+pk_jkgs+"' ");
//					}else if(i<bodyorg.size()-1){
//						sqltotal.append(" or pk_org = '"+pk_jkgs+"' ");
//					}else if(i==bodyorg.size()-1){
//						sqltotal.append(" and pk_org = '"+pk_jkgs+"' ");
//					}
//					i++;
//				}
//				sqltotal.append("and nvl(dr,0)=0 and pk_billtypecode = '36FB' and pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) ");
//			}
			//判断是否为主借款人
			String sql = "select pk_org from cdm_contract where pk_org='"
					+ pk_org + "' and nvl(dr,0)=0 and pk_billtypecode = '36FB' ";
			String headorg = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
//			if(StringUtils.isNotBlank(headorg)){
//				sqltotal.append(" and pk_org = '" + headorg + "'");
//			}
			//判断是否为联合借款人
			String sqlb = "select pk_org from cdm_contract where pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 and pk_billtypecode = '36FB'";
			String bodyorg = (String) getBaseDAO().executeQuery(sqlb,new ColumnProcessor());
			if(StringUtils.isNotBlank(bodyorg)&&StringUtils.isBlank(headorg)){//有联合借款人,没有主借款人
				sqltotal.append(" and (nvl(dr,0)=0 and pk_billtypecode = '36FB' and pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) ");
			}else if(StringUtils.isNotBlank(bodyorg)&&StringUtils.isNotBlank(headorg)){//有联合借款人,也有主借款人
				sqltotal.append(" and (nvl(dr,0)=0) and pk_billtypecode = '36FB' and ( pk_org = '"+pk_org+"' or pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0)) ");
			}else if(StringUtils.isBlank(bodyorg)&&StringUtils.isBlank(headorg)){
				sqltotal.append(" and pk_org = '" + headorg + "'");
			}
		} 
		catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return StringUtils.isBlank(sqltotal.toString())?"  ":sqltotal.toString();
	}
	
	@Override
	public boolean checkTotalOrg(String pk_org) {
		String sql = "select pk_org from cdm_contract where pk_org='"
				+ pk_org + "' and nvl(dr,0)=0 and pk_billtypecode = '36FB' ";
		String sqlb = "select pk_org from cdm_contract where pk_contract in (select pk_contract from cdm_lhjkr where pk_jkgs='"+pk_org+"' and nvl(dr,0)=0) and nvl(dr,0) = 0 and pk_billtypecode = '36FB'";
		//判断是否为主借款人
		try {
			String headorg = (String) getBaseDAO().executeQuery(sql,new ColumnProcessor());
			if(StringUtils.isNotBlank(headorg)){
				return true;
			}
			//判断是否为联合借款人
			List<String> bodyorg = (List<String>) getBaseDAO().executeQuery(sqlb,new ColumnListProcessor());
			if(bodyorg!=null&&bodyorg.size()>0){
				return false;
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}
	
	public BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		
		return dao;
	}
	
}
