/**
 * <p>Title: IncomeInvoiceConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��19�� ����10:50:46

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
 * ����ʱ�䣺2019��9��19�� ����10:50:46  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�IncomeInvoiceConvertor.java  
 * ��˵����  
 */

/**
 * <p>Title: IncomeInvoiceConvertor<��p>

 * <p>Description: <��p>

 * <p>Company: hanzhi<��p> 

 * @author bobo

 * @date 2019��9��19�� ����10:50:46
 */

public class IncomeInvoiceConvertor extends DefaultConvertor{

	@Override
	public String getRefAttributePk(String attribute, String... conditions)throws BusinessException {
		if (conditions == null || conditions.length == 0)
			return null;
		// �׸���������Ĭ�ϱ���
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
					throw new BusinessException("��Ʊ���ͱ���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��Ʊ���ͱ���" + code + "δ����NC�����й���");
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
					throw new BusinessException("�˻�ԭ�����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�˻�ԭ�����" + code + "δ����NC�����й���");
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
					throw new BusinessException("������֯����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("������֯����" + code + "δ����NC�����й���");
			}
		}
		return pkValue;
	}

}
  