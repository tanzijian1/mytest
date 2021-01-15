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

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.convert.DefaultConvertor;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
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

public class FctapOutConvertor extends DefaultConvertor {

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

		if (conditions[0] == null || "".equals(conditions[0])) {
			return null;
		}

		// �׸���������Ĭ�ϱ���
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();
		if ("fct_ap-supplier".equals(attribute)) {
			// ����code��ѯ��Ӧ�̻�����Ϣ
			StringBuffer query = new StringBuffer();
			query.append("SELECT pk_supplier  ");
			query.append("  FROM bd_supplier  ");
			query.append(" WHERE CODE = ?  ");
			query.append("   and enablestate = '2'  ");
			query.append("   and dr = 0  ");

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(query.toString(),
						parameter, new ColumnProcessor());
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}

		// else if ("fct_ap-second".equals(attribute)) {
		// // ��Ӧ��
		// sql = "SELECT pk_supplier FROM bd_supplier WHERE "
		// +
		// "CODE = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
		// +
		// "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
		// + "pk_org = ? ))))";
		// try {
		// for (String condition : conditions) {
		// parameter.addParam(condition);
		// }
		// pkValue = (String) dao.executeQuery(sql, parameter,
		// new ColumnProcessor());
		// // if (pkValue == null) {
		// // throw new BusinessException("��ͷ�ҷ�����Ӧ�̣�����" + code
		// // + "δ����NC�����й���");
		// // }
		// } catch (DAOException e) {
		// throw new BusinessException(e.getMessage());
		// }
		// }
		// if ("fct_ap-third".equals(attribute)) {
		// // ��Ӧ��
		// sql = "SELECT pk_supplier FROM bd_supplier WHERE "
		// +
		// "CODE = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
		// +
		// "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
		// + "pk_org = ? ))))";
		// try {
		// for (String condition : conditions) {
		// parameter.addParam(condition);
		// }
		// pkValue = (String) dao.executeQuery(sql, parameter,
		// new ColumnProcessor());
		// // if (pkValue == null) {
		// // throw new BusinessException("��ͷ��������Ӧ�̣�����" + code
		// // + "δ����NC�����й���");
		// // }
		// } catch (DAOException e) {
		// throw new BusinessException(e.getMessage());
		// }
		// }
		if ("fct_ap-fourth".equals(attribute)) {
			// ��Ӧ��
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
				// if (pkValue == null) {
				// throw new BusinessException("��ͷ��������Ӧ�̣�����" + code
				// + "δ����NC�����й���");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}
		if ("fct_ap-fifth".equals(attribute)) {
			// �췽
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
				// if (pkValue == null) {
				// throw new BusinessException("��ͷ�췽����Ӧ�̣�����" + code
				// + "δ����NC�����й���");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}
		if ("fct_ap-sixth".equals(attribute)) {
			// �ѷ�
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
				// if (pkValue == null) {
				// throw new BusinessException("��ͷ�ѷ�����Ӧ�̣�����" + code
				// + "δ����NC�����й���");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}

		else if ("fct_ap-personnelid".equals(attribute)) {
			// ������
			sql = "select bd_psndoc.pk_psndoc from bd_psndoc left join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc  where "
					+ "bd_psndoc.code = ?  and (enablestate = 2)  and ("
					+ "bd_psnjob.pk_org = ? and (bd_psnjob.enddutydate ='~' or bd_psnjob.enddutydate is null))  and ( "
					+ "bd_psnjob.pk_dept = ? )";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ�����˱���" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ�����˱���" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap-conadmin".equals(attribute)) {
			// ��ͬ������
			sql = "select bd_psndoc.pk_psndoc from bd_psndoc left join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc  where "
					+ "bd_psndoc.code = ?  and (enablestate = 2)  and ("
					+ "(bd_psnjob.enddutydate ='~' or bd_psnjob.enddutydate is null))  and ( "
					+ "bd_psnjob.pk_dept = ? )";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ��ͬ�����˱���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ��ͬ�����˱���" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap-grade".equals(attribute)) {
			// ���죨�а죩ְλ ���գ���λ��
			sql = "select pk_post from om_post  where "
					+ "postcode = ?  and (enablestate = 2)  and (("
					+ "pk_org = ?))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ���죨�а죩ְλ����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ���죨�а죩ְλ����" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap-accountorg".equals(attribute)) {
			// ���˹�˾(���ղ�����֯)
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ���˹�˾(���ղ�����֯)����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ���˹�˾(���ղ�����֯)����" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap-pk_org".equals(attribute)) {
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
					throw new BusinessException("��ͷ������֯����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ������֯����" + code + "δ����NC�����й���");
			}
		} else if ("fct_ap_plan-compensatecor".equals(attribute)) {
			// �ֳ���˹�˾������ƻ�ǩԼ�� ���ղ�����֯
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("���帶��ƻ���ǩԼ���ֳ���˹�˾�����ղ�����֯������"
							+ code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("���帶��ƻ���ǩԼ���ֳ���˹�˾�����ղ�����֯������"
						+ code + "δ����NC�����й���");
			}
		} else if ("fct_ap-subbudget".equals(attribute)) {
			// Ԥ������(���ղ�����֯)
			sql = "select PK_PLANBUDGET from org_planbudget where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷԤ������(���ղ�����֯)����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷԤ������(���ղ�����֯)����" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_pmplan-offsetcompany".equals(attribute)) {
			// �ֳ���˹�˾(����ƻ�Ѻ��) ���ղ�����֯
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException(
							"���帶��ƻ�����֤��_Ѻ��_�����_�����ʽ𣩵ֳ���˹�˾�����ղ�����֯������" + code
									+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException(
						"���帶��ƻ�����֤��_Ѻ��_�����_�����ʽ𣩵ֳ���˹�˾�����ղ�����֯������" + code
								+ "δ����NC�����й���");
			}
		} else if ("fct_ap-depid".equals(attribute)) {
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
		} else if ("fct_moretax-maininvoicetype".equals(attribute)) {
			// ��˰��-��Ҫ��Ʊ����
			sql = "select  pk_defdoc,pk_defdoc from bd_defdoc  where "

					+ "def1 = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'pjlx'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����˰����Ҫ��Ʊ���ͱ���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����˰����Ҫ��Ʊ���ͱ���" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap-plate".equals(attribute)) {
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
		} else if ("fct_pmplan-paytype".equals(attribute)) {
			// ��������-����ƻ�����֤��Ѻ�𡢳����͹����ʽ�
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and (pk_org = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy020')";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("���帶��ƻ�����֤��Ѻ�𡢳����͹����ʽ𣩿������ͱ���"
							+ code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("���帶��ƻ�����֤��Ѻ�𡢳����͹����ʽ𣩿������ͱ���"
						+ code + "δ����NC�����й���");
			}
		} else if ("fct_ap_plan-def2".equals(attribute)) {
			// ��������-����ƻ���ǩԼ��
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_org = '000112100000000005FD')  and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy020'))";
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
			// ��Ŀ���ƣ�Ԥ���Ŀ��-��ͬ�������ɱ���֣�
			sql = "select pk_obj from tb_budgetsub  where "
					+ "objcode = ?  and (("
					+ "pk_org = ? or pk_group = '000112100000000005FD'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣���Ŀ���ƣ�Ԥ���Ŀ������"
							+ code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣���Ŀ���ƣ�Ԥ���Ŀ������" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap_b-vbdef15".equals(attribute)) {
			// ҵ̬-��ͬ�������ɱ���֣�
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "pk_defdoc = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'ys004'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣�ҵ̬����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣�ҵ̬����" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap_b-vbdef16".equals(attribute)) {
			// ��������-��ͬ�������ɱ���֣�
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD')  and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'ys008'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣��������ű���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣��������ű���" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap_b-vbdef17".equals(attribute)) {
			// ����/¥��-��ͬ�������ɱ���֣�
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'lcbm'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣�����/¥�����" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣�����/¥�����" + code
						+ "δ����NC�����й���");
			}
		} else if ("fct_ap_b-vbdef18".equals(attribute)) {
			// �ÿ���-��ͬ�������ɱ���֣�
			// sql =
			// "select bd_psndoc.pk_psndoc from bd_psndoc left join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc  where "
			// + "bd_psndoc.code = ?  and (enablestate = 2)  and ("
			// +
			// "bd_psnjob.pk_org = ? and (bd_psnjob.enddutydate ='~' or bd_psnjob.enddutydate is null))  and ( "
			// + "bd_psnjob.pk_dept = ? )";
			sql = "select distinct bd_psndoc.pk_psndoc from bd_psndoc left join bd_psnjob on bd_psndoc.pk_psndoc = bd_psnjob.pk_psndoc where bd_psndoc.code = ? and (enablestate = 2)";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("�����ͬ�������ɱ���֣��ÿ��˱���" + code
							+ "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("�����ͬ�������ɱ���֣��ÿ��˱���" + code
						+ "δ����NC�����й���");
			}

		}
		// ��ӱ���2019-10-25-tzj
		else if ("fct_ap-corigcurrencyid".equals(attribute)) {
			// ����
			sql = "select PK_CURRTYPE name from bd_currtype b where b.pk_currtype = ? and dr = 0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ����" + code + "δ����NC�����й���");
				}
			} catch (DAOException e) {
				throw new BusinessException("��ͷ����" + code + "δ����NC�����й���");
			}

		}

		return pkValue;
	}

}
