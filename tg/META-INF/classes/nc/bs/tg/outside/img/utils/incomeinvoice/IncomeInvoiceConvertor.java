/**
 * <p>Title: IncomeInvoiceConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月19日 下午10:50:46

 * @version 1.0
 */   

package nc.bs.tg.outside.img.utils.incomeinvoice;   

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

/**
 * 创建时间：2019年9月19日 下午10:50:46  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：IncomeInvoiceConvertor.java  
 * 类说明：  
 */

/**
 * <p>Title: IncomeInvoiceConvertor<／p>

 * <p>Description: <／p>

 * <p>Company: hanzhi<／p> 

 * @author bobo

 * @date 2019年9月19日 下午10:50:46
 */

public class IncomeInvoiceConvertor extends DefaultConvertor{

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
		if ("InvoiceVO-fplx".equals(attribute)) {
			sql = "select pk_invoicetype from hzvat_invoicetype  where "
					+ "code = ?";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("发票类型编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("发票类型编码" + code + "未能在NC档案中关联");
			}
		}
		else if("InvoiceVO-thyy".equals(attribute)){
			sql = "select pk_id from hzvat_backreason  where "
					+ "code = ?";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("退回原因编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("退回原因编码" + code + "未能在NC档案中关联");
			}
		}
		else if ("InvoiceVO-pk_org".equals(attribute)) {
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("财务组织编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("财务组织编码" + code + "未能在NC档案中关联");
			}
		}
		return pkValue;
	}

}
  