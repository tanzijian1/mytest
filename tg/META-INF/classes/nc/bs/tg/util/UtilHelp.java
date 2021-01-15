package nc.bs.tg.util;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;


/**
 * 
 * @author lyq
 * 
 */
@SuppressWarnings("unchecked")
public class UtilHelp {

	public UtilHelp() {
		
	}

	BaseDAO dao;

	public BaseDAO getBaseDao() {

		if (dao != null) {
			return dao;
		}

		dao = new BaseDAO();

		return dao;
	}
	
	
	
	
	
	/**
	 * 公司名称和编码
	 * 
	 * @param pk_org_v
	 * @return orgMsg   orgMsg[0]:code  orgMsg[1]:name
	 * @throws BusinessException 
	 */
	public Object[] getOrgmsg(String pk_org) throws BusinessException {
		String sql = "select o.code ,o.name from org_financeorg o where o.pk_org='"
				+ pk_org + "'";
		Object[] orgMsg=null;
			orgMsg = (Object[]) getBaseDao().executeQuery(sql, new ArrayProcessor());
			if(orgMsg==null){
				throw new BusinessException("组织信息查询失败，请检查！");
			}
		return orgMsg;
	}
	
	

	/**
	 * 部门名称和编码
	 * 
	 * @param pk_dept
	 * @return deptMsg   deptMsg[0]:code  deptMsg[1]:name
	 * @throws BusinessException 
	 */
	public Object[] getDeptmsg(String pk_dept) throws BusinessException {
		String sql = "select o.code ,o.name from org_dept o where o.pk_dept='"
				+ pk_dept + "'";
		Object[] deptMsg=null;
		deptMsg = (Object[]) getBaseDao().executeQuery(sql, new ArrayProcessor());
			if(deptMsg==null){
				throw new BusinessException("部门信息查询失败，请检查！");
			}
		return deptMsg;
	}
	/**
	 * 获取人员名称
	 * 
	 * @param pk_person
	 * @return person_name 
	 */
	public String getPerson_name(String pk_person) {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ pk_person + "'";
		String person_name=null;
		try {
			person_name = (String) getBaseDao().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return person_name;
	}

	
	
	/**
	 * 获取项目名称
	 * 
	 * @param pk_project
	 * @return projectMsg  projectMsg[0]=code  projectMsg[1]=name 
	 */
	public Object[] getProject_name(String pk_project) {
		String sql = "select b.project_code,b.project_name from bd_project b where b.pk_project='"
				+ pk_project + "'";
		Object[] projectMsg=null;
		try {
			projectMsg = (Object[]) getBaseDao().executeQuery(sql, new ArrayProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return projectMsg;
	}
	
	
	/**
	 * 客户信息
	 * 
	 * @param pk_customer
	 * @return
	 */
	public Object[] getCustomerMsg(String pk_customer) {
		String sql = "select b.code,b.name from bd_customer b where b.pk_customer='"
				+ pk_customer + "'";
		Object[] customerMsg=null;
		try {
			customerMsg = (Object[]) getBaseDao().executeQuery(sql, new ArrayProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return customerMsg;
	}

	
	/**
	 * 流程类别编码和名称
	 * 
	 * @param pk_defdoc
	 * @return flowMsg   flowMsg[0]:流程类别编码  flowMsg[1]:流程类别名称
	 */
	public Object[] getFlowMsg(String pk_defdoc) {
		String sql = "select b.code,b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_defdoc + "'";
		Object[] flowMsg=null;
		try {
			flowMsg = (Object[]) getBaseDao().executeQuery(sql, new ArrayProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return flowMsg;
	}

	/**
	 * 合同名称
	 * 
	 * @param pk_defdoc
	 * @return contract_name
	 */
	public String getContract_name(String pk_defdoc) {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_defdoc + "'";
		String contract_name=null;
		try {
			contract_name = (String) getBaseDao().executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return contract_name;
	}
	

}
