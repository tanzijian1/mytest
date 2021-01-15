/**
 * <p>Title: ExHouseTransferBillConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��16�� ����3:36:18

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
 * ����ʱ�䣺2019��9��16�� ����3:36:18  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�ExHouseTransferBillConvertor.java  
 * ��˵����  
 */

/**
 * <p>Title: ExHouseTransferBillConvertor<��p>

 * <p>Description: <��p>

 * <p>Company: hanzhi<��p> 

 * @author bobo

 * @date 2019��9��16�� ����3:36:18
 */

public class ExHouseTransferBillConvertor extends DefaultConvertor{

	/* (non-Javadoc)
	 * <p>Title: getRefAttributePk<��p>
	 * <p>Description: <��p>
	 * @param attribute
	 * @param code
	 * @return
	 * @see nc.bs.tg.outside.sale.utils.exhousetransfer.DefaultConvertor#getRefAttributePk(java.lang.String, java.lang.String)
	 */
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
		if("exhousetransferbillHVO-billmaker".equals(attribute)){
			sql = "select cuserid from sm_user where user_code = ? and nvl(dr,0) = 0";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("��ͷ�Ƶ��ˣ��û�������" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�Ƶ��ˣ��û�������" + code + "δ����NC�����й���");
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
					throw new BusinessException("��ͷ������֯����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ������֯����" + code + "δ����NC�����й���");
			}
		}
		
		return pkValue;
	}

}
  