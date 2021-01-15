/**
 * <p>Title: ExHouseTransferBillConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月16日 下午3:36:18

 * @version 1.0
 */   

package nc.bs.tg.outside.sale.utils.exhousetransfer;   

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

/**
 * 创建时间：2019年9月16日 下午3:36:18  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：ExHouseTransferBillConvertor.java  
 * 类说明：  
 */

/**
 * <p>Title: ExHouseTransferBillConvertor<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月16日 下午3:36:18
 */

public class ExHouseTransferBillConvertor extends DefaultConvertor{

	/* (non-Javadoc)
	 * <p>Title: getRefAttributePk<／p>
	 * <p>Description: <／p>
	 * @param attribute
	 * @param code
	 * @return
	 * @see nc.bs.tg.outside.sale.utils.exhousetransfer.DefaultConvertor#getRefAttributePk(java.lang.String, java.lang.String)
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
		if("exhousetransferbillHVO-billmaker".equals(attribute)){
			sql = "select cuserid from sm_user where user_code = ? and nvl(dr,0) = 0";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("表头制单人（用户）编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头制单人（用户）编码" + code + "未能在NC档案中关联");
			}
		}
		else if ("exhousetransferbillHVO-pk_org".equals(attribute)) {
			sql = "select pk_financeorg from org_financeorg where code = ? and nvl(dr,0) = 0";
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
		
		return pkValue;
	}

}
  