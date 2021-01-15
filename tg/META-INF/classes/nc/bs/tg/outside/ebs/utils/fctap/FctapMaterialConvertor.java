/**
 * <p>Title: FctapMaterialConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��29�� ����2:37:34

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
 * ����ʱ�䣺2019��9��29�� ����2:37:34  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�FctapMaterialConvertor.java  
 * ��˵����  
 */

/**
 * <p>Title: FctapMaterialConvertor<��p>

 * <p>Description: <��p>

 * <p>Company: hanzhi<��p> 

 * @author bobo

 * @date 2019��9��29�� ����2:37:34
 */

public class FctapMaterialConvertor extends DefaultConvertor{

	/* (non-Javadoc)
	 * <p>Title: getRefAttributePk<��p>
	 * <p>Description: <��p>
	 * @param attribute
	 * @param code
	 * @return
	 * @see nc.bs.tg.outside.convert.DefaultConvertor#getRefAttributePk(java.lang.String, java.lang.String)
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
		
		if("fct_ap-personnelid".equals(attribute)){
			// ������
			sql = "select bd_psndoc.pk_psndoc from bd_psndoc  where "
					+ "bd_psndoc.code = ?  and (enablestate = 2) ";
			try {
				for(String condition : conditions){
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter, 
						new ColumnProcessor());
				if(pkValue == null){
					throw new BusinessException("��ͷ�����˱���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�����˱���" + code + "δ����NC�����й���");
			}
		}
		else if ("fct_ap-pk_org".equals(attribute)) {
			// ������֯
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
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
		else if ("fct_ap-cvendorid".equals(attribute)) {
			// ��Ӧ������ ���չ�Ӧ��
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
					throw new BusinessException("��ͷ��Ӧ�����Ʊ���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ��Ӧ�����Ʊ���" + code + "δ����NC�����й���");
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
					throw new BusinessException("��ͷ�������ͱ���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�������ͱ���" + code + "δ����NC�����й���");
			}
		}
		
		return pkValue;
	}

}
  