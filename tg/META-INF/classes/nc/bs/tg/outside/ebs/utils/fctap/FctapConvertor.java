/**
 * <p>Title: FctapConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月24日 下午2:58:30

 * @version 1.0
 */   

package nc.bs.tg.outside.ebs.utils.fctap;   

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;

/**
 * 创建时间：2019年9月24日 下午2:58:30  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：FctapConvertor.java  
 * 类说明：  
 */

/**
 * <p>Title: FctapConvertor<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月24日 下午2:58:30
 */

public class FctapConvertor extends DefaultConvertor{

	/* (non-Javadoc)
	 * <p>Title: getRefAttributePk<／p>
	 * <p>Description: <／p>
	 * @param attribute
	 * @param code
	 * @return
	 * @see nc.bs.tg.outside.convert.DefaultConvertor#getRefAttributePk(java.lang.String, java.lang.String)
	 */
	@Override
	public String getRefAttributePk(String attribute, String code) {
		if (code == null || "".equals(code))
			return null;
		
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();
		if ("cvendorid".equals(attribute)) {
			sql = "select pk_supplier from bd_supplier where code = ? and nvl(dr,0) = 0";
		}
		else if("personnelid".equals(attribute)){
			sql = "select  pk_psndoc from bd_psndoc where code = ? and nvl(dr,0) = 0";
		}
		else if ("pk_org".equals(attribute)) {
			sql = "select pk_financeorg from org_financeorg where code = ? and nvl(dr,0) = 0";
		}
		else if ("depid".equals(attribute)) {
			sql = "select pk_dept from org_dept where code = ? and nvl(dr,0) = 0";
		}
		else if ("ctrantypeid".equals(attribute)) {
			sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE=?";
		}
		try {
			parameter.addParam(code);
			pkValue = (String) dao.executeQuery(sql, parameter, 
					new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return pkValue;
	}

}
  