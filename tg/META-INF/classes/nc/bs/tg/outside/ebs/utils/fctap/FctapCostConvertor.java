/**
 * <p>Title: FctapConvertor.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��24�� ����2:58:30

 * @version 1.0
 */

package nc.bs.tg.outside.ebs.utils.fctap;

import java.util.Map;

import jxl.write.Blank;

import org.apache.poi.hssf.util.HSSFColor.BLACK;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;

/**
 * ����ʱ�䣺2019��9��24�� ����2:58:30  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�FctapConvertor.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: FctapConvertor<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��24�� ����2:58:30
 */

public class FctapCostConvertor extends DefaultConvertor {

	/*
	 * (non-Javadoc) <p>Title: getRefAttributePk<��p> <p>Description: <��p>
	 * 
	 * @param attribute
	 * 
	 * @param code
	 * 
	 * @return
	 * 
	 * @see
	 * nc.bs.tg.outside.convert.DefaultConvertor#getRefAttributePk(java.lang
	 * .String, java.lang.String)
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

		if ("fct_ap-plate".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}

			// ���
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ( pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'bkxx'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ������" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ������" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap-proname".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ��Ŀ����
			sql = "select  bd_project.pk_project from bd_project bd_project  left   join  bd_project_b b on bd_project.PK_PROJECT=b.PK_PROJECT   where "
					+ "project_code = ? and ( bd_project.dr = 0  ) and (enablestate = 2)  and ( deletestate is null or deletestate<>1) and (   b.dr = 0)  and ("
					+ "b.PK_PARTI_ORG=?  or " + "b.PK_ORG =? )";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ��Ŀ���Ʊ���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ��Ŀ���Ʊ���" + code + "δ����NC�����й���");
			}
		}
		// else if ("fct_ap-pk_org".equals(attribute)) {
		// if (conditions[0] == null || "".equals(conditions[0])) {
		// return null;
		// }
		// // ������֯
		// sql = "select pk_financeorg from org_financeorg where "
		// + "code = ? and nvl(dr,0) = 0 and enablestate = 2";
		// try {
		// for (String condition : conditions) {
		// parameter.addParam(condition);
		// }
		// pkValue = (String) dao.executeQuery(sql, parameter,
		// new ColumnProcessor());
		// if (pkValue == null) {
		// throw new BusinessException("��ͷ������֯����" + code
		// + "δ����NC�����й���");
		// }
		// } catch (DAOException e) {
		// throw new BusinessException("��ͷ������֯����" + code + "δ����NC�����й���");
		// }
		// }
		else if ("fct_ap-pk_org".equals(attribute)) { // ������֯
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			sql = "select o.pk_org,o.def3 from org_orgs o where o.pk_org = ? and dr = 0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				Map<String, Object> pkValues = (Map<String, Object>) dao
						.executeQuery(sql, parameter, new MapProcessor());
				if (pkValues != null) {
					// ����Ƿ���������֯
					String def3 = (String) pkValues.get("def3");
					String pk_org = (String) pkValues.get("pk_org");
					if (def3 != null && !"".equals(def3))
						// 100112100000000005KE��������֯
						if ("100112100000000005KE".equals(def3)) {
							pkValue = (String) pkValues.get("pk_org");
						} else {
							Map<String, Object> pkValues_1 = null;
							while (!"100112100000000005KE".equals(def3)) {
								sql = "select pk_org,def3 from org_orgs where pk_org = (select o.pk_fatherorg from org_orgs o where o.pk_org = '"
										+ pk_org + "' and dr = 0) and dr = 0";
								pkValues_1 = (Map<String, Object>) dao
										.executeQuery(sql, new MapProcessor());

								String def3_1 = (String) pkValues_1.get("def3");
								String pk_org_1 = (String) pkValues_1
										.get("pk_org");
								if(!"100112100000000005KE".equals(def3_1)){
									pk_org = pk_org_1;
								}
								if (def3_1 != null && !"".equals(def3_1)) {
									def3 = def3_1;
								} else {
									throw new BusinessException("�׷�:"
											+ conditions[0] + "�������й�˾ʧ��");
								}
								if ("00011210000000000ZI0".equals(pk_org_1)) {
									throw new BusinessException(
											"�Ҳ�������֯��Ӧ�ĳ��й�˾!");
								}
							}
							if (pkValues_1 != null) {
								pkValue = (String) pkValues_1.get("pk_org");
							}
						}
				}

				if (pkValue == null) {
					throw new BusinessException("��ͷ���й�˾" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ���й�˾" + code + "δ����NC�����й���");
			}

		} else if ("fct_ap-depid".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ���첿��
			sql = "select pk_dept from org_dept  where "
					+ "code = ? and (enablestate = 2)  and ((pk_group = '000112100000000005FD' and "
					+ "pk_org = ?))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ���첿�ű���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ���첿�ű���" + code + "δ����NC�����й���");
			}
		} else if ("ctrantypeid".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ��������
			sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE=?";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ�������ͱ���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�������ͱ���" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap-vdef1".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ҵ̬
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'ys004'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷҵ̬����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷҵ̬����" + code + "δ����NC�����й���");
			}
		}

		else if ("fct_ap-first".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// �׷�
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ�׷���������֯������" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�׷���������֯������" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap-second".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// �ҷ�
			sql = "SELECT pk_supplier FROM bd_supplier WHERE "
					+ "CODE = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
					+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
					+ "pk_org = ? ))))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ�ҷ�����Ӧ�̣�����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�ҷ�����Ӧ�̣�����" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap_plan-def2".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ��������-����ƻ���ǩԼ��
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_org = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy020'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("���帶��ƻ���ǩԼ���������ͱ���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("���帶��ƻ���ǩԼ���������ͱ���" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap_b-vbdef11".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ��Ŀ���ƣ�Ԥ���Ŀ��-��ͬ�������ɱ���֣�
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_org = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy024'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣���Ŀ���ƣ��ɱ���Ŀ������"
							+ code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣���Ŀ���ƣ�Ԥ���Ŀ������" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap-vdef12".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			// ͨ����ͬ�����˱����������
			sql = "select c.name from bd_psndoc c where c.code = ? and c.enablestate = 2 and dr = 0";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
//				if (pkValue == null) {
//					throw new BusinessException("��ͬ�����˱���" + code + "δ����NC�����й���");
//				}
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		} else if ("fct_ap-def100".equals(attribute)) {
			StringBuffer query = new StringBuffer();
			query.append("select pk_defdoc  ");
			query.append("  from bd_defdoc  ");
			query.append(" where code = ?  ");
			query.append("   and (enablestate = 2)  ");
			query.append("   and (pk_defdoclist =  ");
			query.append("       (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy044'))  ");
			for (String condition : conditions) {
				parameter.addParam(condition);
			}
			pkValue = (String) dao.executeQuery(query.toString(), parameter,
					new ColumnProcessor());
		} else if ("fct_ap-def101".equals(attribute)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			StringBuffer query = new StringBuffer();
			query.append("select pk_defdoc  ");
			query.append("  from bd_defdoc  ");
			query.append(" where code = ?  ");
			query.append("   and (enablestate = 2)  ");
			query.append("   and (pk_defdoclist =  ");
			query.append("       (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy042'))  ");

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(query.toString(),
						parameter, new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("ҵ����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}
		return pkValue;
	}
}
