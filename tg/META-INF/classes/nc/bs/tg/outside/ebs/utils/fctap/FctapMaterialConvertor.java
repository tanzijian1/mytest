/**
 * <p>Title: FctapMaterialConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月29日 下午2:37:34

 * @version 1.0
 */   

package nc.bs.tg.outside.ebs.utils.fctap;   

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

/**
 * 创建时间：2019年9月29日 下午2:37:34  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：FctapMaterialConvertor.java  
 * 类说明：  
 */

/**
 * <p>Title: FctapMaterialConvertor<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月29日 下午2:37:34
 */

public class FctapMaterialConvertor extends DefaultConvertor{

	/* (non-Javadoc)
	 * <p>Title: getRefAttributePk<／p>
	 * <p>Description: <／p>
	 * @param attribute
	 * @param code
	 * @return
	 * @see nc.bs.tg.outside.convert.DefaultConvertor#getRefAttributePk(java.lang.String, java.lang.String)
	 */
	@Override
	public String getRefAttributePk(String attribute, String... conditions)throws BusinessException {
		if (conditions == null || conditions.length == 0)
			return null;
		// 首个条件参数默认编码
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();
		
		if("fct_ap-personnelid".equals(attribute)){
			// 经办人
			sql = "select bd_psndoc.pk_psndoc from bd_psndoc  where "
					+ "bd_psndoc.code = ?  and (enablestate = 2) ";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("表头经办人编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头经办人编码" + code + "未能在NC档案中关联");
			}
		}
		else if ("fct_ap-pk_org".equals(attribute)) {
			// 财务组织
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("表头财务组织编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头财务组织编码" + code + "未能在NC档案中关联");
			}
		}
		else if ("fct_ap-cvendorid".equals(attribute)) {
			// 供应商名称 参照供应商
			sql = "SELECT pk_supplier FROM bd_supplier WHERE "
					+ "CODE = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
					+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
					+ "pk_org = ? ))))";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("表头供应商名称编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头供应商名称编码" + code + "未能在NC档案中关联");
			}
		}
		else if ("ctrantypeid".equals(attribute)) {
			sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE=?";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("表头交易类型编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头交易类型编码" + code + "未能在NC档案中关联");
			}
		}
		
		return pkValue;
	}

}
  