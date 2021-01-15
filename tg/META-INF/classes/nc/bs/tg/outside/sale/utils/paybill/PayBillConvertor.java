/**
 * <p>Title: PayBillConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��16�� ����3:36:18

 * @version 1.0
 */

package nc.bs.tg.outside.sale.utils.paybill;

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
 * �ļ����ƣ�PayBillConvertor.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: PayBillConvertor<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��16�� ����3:36:18
 */

public class PayBillConvertor extends DefaultConvertor {

	/*
	 * (non-Javadoc) <p>Title: getRefAttributePk<��p> <p>Description: <��p>
	 * 
	 * @param attribute
	 * 
	 * @param code
	 * 
	 * @return
	 * 
	 * @see nc.bs.tg.outside.sale.utils.exhousetransfer.DefaultConvertor#
	 * getRefAttributePk(java.lang.String, java.lang.String)
	 */
	@Override
	public String getRefAttributePk(String attribute, String... conditions)
			throws BusinessException {
		if (conditions == null || conditions.length == 0)
			return null;
		// �׸���������Ĭ�ϱ���
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();

		if ("paybill-supplier".equals(attribute)) {
			// ��ͷ��Ӧ�̣��ͻ������̣�
			sql = "SELECT pk_supplier FROM bd_supplier WHERE "
					+ " name = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
					+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
					+ "pk_org = ? ))))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		} else if ("paybill-pk_org".equals(attribute)) {
			// ������֯
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("������֯����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("������֯����" + code + "δ����NC�����й���");
			}
		} else if ("paybill-pk_tradetypeid".equals(attribute)) {
			// ��������
			sql = "select pk_billtypeid  from bd_billtype where "
					+ "pk_billtypecode = ? and nvl(dr,0) = 0 and pk_group = '000112100000000005FD'";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��������" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��������" + code + "δ����NC�����й���");
			}
		} else if ("paybill-pk_currtype".equals(attribute)) {
			// ����
			sql = "select pk_currtype from bd_currtype where "
					+ "code = ? and nvl(dr,0) = 0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("���ֱ���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("���ֱ���" + code + "δ����NC�����й���");
			}
		}

		else if ("payitem-pk_balatype".equals(attribute)) {
			// ���㷽ʽ
			sql = "select pk_balatype from bd_balatype  where "
					+ "(code = ? or name = ?) and ( isnull(directincome, 'N') = 'N' or  isnull(directincome, '~') = '~') and (enablestate = 2)";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("���㷽ʽ����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("���㷽ʽ����" + code + "δ����NC�����й���");
			}
		}

		else if ("payitem-payaccount".equals(attribute)) {
			// ���������˻�
			sql = "select pk_bankaccsub from bd_bankaccsub where pk_bankaccbas = "
				+ "(select pk_bankaccbas from bd_bankaccbas where ( accclass = 2 ) "
				+ "and ( enablestate in ( 2, 1 ) and ( pk_group = '000112100000000005FD' AND "
				+ "accnum = ? and pk_org = ?))) ";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				/*
				 * if(pkValue == null){ throw new BusinessException("���������˻�����" +
				 * code + "δ����NC�����й���"); }
				 */
			} catch (DAOException e) {
				// throw new BusinessException("���������˻�����" + code +
				// "δ����NC�����й���");
			}
		}

		else if ("payitem-supplier".equals(attribute)) {
			// ���幩Ӧ�̣��ͻ������̣�
			sql = "SELECT pk_supplier FROM bd_supplier WHERE "
					+ " and name= ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
					+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
					+ "pk_org = ? ))))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�ͻ�����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�ͻ�����" + code + "δ����NC�����й���");
			}
		} else if ("payitem-recaccount".equals(attribute)) {
			// �ͻ������˺�
			sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = ? "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and (pk_cust = ?  and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust))";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�ͻ������˺�" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�ͻ������˺�" + code + "δ����NC�����й���");
			}
		} else if ("payitem-def11".equals(attribute)) {
			// �տ������˻�������

			sql = "select pk_banktype from bd_banktype where "
					+ "code = ? and nvl(dr,0) = 0";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				// if(pkValue == null){
				// throw new BusinessException("�տ������˻������б���" + code +
				// "δ����NC�����й���");
				// }
			} catch (DAOException e) {
				// throw new BusinessException("�տ������˻������б���" + code +
				// "δ����NC�����й���");
			}
		} else if ("payitem-def21".equals(attribute)) {
			// ������Ŀ(NC���ز���Ŀ)
			sql = "select  bd_project.pk_project from bd_project bd_project where "
					+ "def2 = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
//				if (pkValue == null) {
//					throw new BusinessException("������Ŀ(NC���ز���Ŀ)����" + code
//							+ "δ����NC�����й���");
//				}
			} catch (DAOException e) {
				throw new BusinessException("������Ŀ(NC���ز���Ŀ)����" + code
						+ "δ����NC�����й���");
			}
		} else if ("payitem-def22".equals(attribute)) {
			// ���򷿼䣨NC���ز���Ŀ��ϸ��
			sql = "select bd_project.pk_project from bd_project bd_project where "
					+ "def2 = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
//				if (pkValue == null) {
//					throw new BusinessException("���򷿼䣨NC���ز���Ŀ��ϸ������" + code
//							+ "δ����NC�����й���");
//				}
			} catch (DAOException e) {
				throw new BusinessException("���򷿼䣨NC���ز���Ŀ��ϸ������" + code
						+ "δ����NC�����й���");
			}
		} else if ("payitem-def23".equals(attribute)) {
			// ��������
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�������ͱ���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�������ͱ���" + code + "δ����NC�����й���");
			}
		} else if ("payitem-def24".equals(attribute)) {
			// ��������
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2) and nvl(dr,0)=0 ";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�������" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�������" + code + "δ����NC�����й���");
			}
		} else if ("payitem-def25".equals(attribute)) {
			// ҵ̬
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "name = ?  and (enablestate = 2)  and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("ҵ̬����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("ҵ̬����" + code + "δ����NC�����й���");
			}
		} else if ("payitem-def26".equals(attribute)) {
			// ��˰��ʽ
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "name = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��˰��ʽ����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��˰��ʽ����" + code + "δ����NC�����й���");
			}
		}

		return pkValue;
	}

}
