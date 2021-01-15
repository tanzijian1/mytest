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
import nc.vo.pub.BusinessException;

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
 * <p>
 * Title: FctapConvertor<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月24日 下午2:58:30
 */

public class FctapOutConvertor extends DefaultConvertor {

	/*
	 * (non-Javadoc) <p>Title: getRefAttributePk<／p> <p>Description: <／p>
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

		// 首个条件参数默认编码
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();
		if ("fct_ap-supplier".equals(attribute)) {
			// 根据code查询供应商基本信息
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
		// // 供应商
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
		// // throw new BusinessException("表头乙方（供应商）编码" + code
		// // + "未能在NC档案中关联");
		// // }
		// } catch (DAOException e) {
		// throw new BusinessException(e.getMessage());
		// }
		// }
		// if ("fct_ap-third".equals(attribute)) {
		// // 供应商
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
		// // throw new BusinessException("表头丙方（供应商）编码" + code
		// // + "未能在NC档案中关联");
		// // }
		// } catch (DAOException e) {
		// throw new BusinessException(e.getMessage());
		// }
		// }
		if ("fct_ap-fourth".equals(attribute)) {
			// 供应商
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
				// throw new BusinessException("表头丁方（供应商）编码" + code
				// + "未能在NC档案中关联");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}
		if ("fct_ap-fifth".equals(attribute)) {
			// 戊方
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
				// throw new BusinessException("表头戊方（供应商）编码" + code
				// + "未能在NC档案中关联");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}
		if ("fct_ap-sixth".equals(attribute)) {
			// 已方
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
				// throw new BusinessException("表头已方（供应商）编码" + code
				// + "未能在NC档案中关联");
				// }
			} catch (DAOException e) {
				throw new BusinessException(e.getMessage());
			}
		}

		else if ("fct_ap-personnelid".equals(attribute)) {
			// 经办人
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
					throw new BusinessException("表头经办人编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头经办人编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_ap-conadmin".equals(attribute)) {
			// 合同管理人
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
					throw new BusinessException("表头合同管理人编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头合同管理人编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_ap-grade".equals(attribute)) {
			// 经办（承办）职位 参照（岗位）
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
					throw new BusinessException("表头经办（承办）职位编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头经办（承办）职位编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap-accountorg".equals(attribute)) {
			// 出账公司(参照财务组织)
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头出账公司(参照财务组织)编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头出账公司(参照财务组织)编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap-pk_org".equals(attribute)) {
			// 财务组织
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头财务组织编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头财务组织编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_ap_plan-compensatecor".equals(attribute)) {
			// 抵冲出账公司（付款计划签约金额） 参照财务组织
			sql = "select pk_financeorg from org_financeorg where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体付款计划（签约金额）抵冲出账公司（参照财务组织）编码"
							+ code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体付款计划（签约金额）抵冲出账公司（参照财务组织）编码"
						+ code + "未能在NC档案中关联");
			}
		} else if ("fct_ap-subbudget".equals(attribute)) {
			// 预算主体(参照财务组织)
			sql = "select PK_PLANBUDGET from org_planbudget where "
					+ "code = ? and nvl(dr,0) = 0 and enablestate = 2";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头预算主体(参照财务组织)编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头预算主体(参照财务组织)编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_pmplan-offsetcompany".equals(attribute)) {
			// 抵冲出账公司(付款计划押金) 参照财务组织
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
							"表体付款计划（保证金_押金_诚意金_共管资金）抵冲出账公司（参照财务组织）编码" + code
									+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException(
						"表体付款计划（保证金_押金_诚意金_共管资金）抵冲出账公司（参照财务组织）编码" + code
								+ "未能在NC档案中关联");
			}
		} else if ("fct_ap-depid".equals(attribute)) {
			// 经办部门
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
					throw new BusinessException("表头经办部门编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头经办部门编码" + code + "未能在NC档案中关联");
			}
		} else if ("ctrantypeid".equals(attribute)) {
			// 交易类型
			sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE=?";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头交易类型编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头交易类型编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_moretax-maininvoicetype".equals(attribute)) {
			// 多税率-主要发票类型
			sql = "select  pk_defdoc,pk_defdoc from bd_defdoc  where "

					+ "def1 = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'pjlx'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体多税率主要发票类型编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体多税率主要发票类型编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap-plate".equals(attribute)) {
			// 板块
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ( pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'bkxx'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头板块编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头板块编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_ap-proname".equals(attribute)) {
			// 项目名称
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
					throw new BusinessException("表头项目名称编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头项目名称编码" + code + "未能在NC档案中关联");
			}
		} else if ("fct_pmplan-paytype".equals(attribute)) {
			// 款项类型-付款计划（保证金、押金、诚意金和共管资金）
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and (pk_org = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy020')";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体付款计划（保证金、押金、诚意金和共管资金）款项类型编码"
							+ code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体付款计划（保证金、押金、诚意金和共管资金）款项类型编码"
						+ code + "未能在NC档案中关联");
			}
		} else if ("fct_ap_plan-def2".equals(attribute)) {
			// 款项类型-付款计划（签约金额）
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_org = '000112100000000005FD')  and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'zdy020'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体付款计划（签约金额）款项类型编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体付款计划（签约金额）款项类型编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap_b-vbdef11".equals(attribute)) {
			// 科目名称（预算科目）-合同基本（成本拆分）
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
					throw new BusinessException("表体合同基本（成本拆分）科目名称（预算科目）编码"
							+ code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体合同基本（成本拆分）科目名称（预算科目）编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap_b-vbdef15".equals(attribute)) {
			// 业态-合同基本（成本拆分）
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "pk_defdoc = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'ys004'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体合同基本（成本拆分）业态主键" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体合同基本（成本拆分）业态编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap_b-vbdef16".equals(attribute)) {
			// 车辆部门-合同基本（成本拆分）
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD')  and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'ys008'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体合同基本（成本拆分）车辆部门编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体合同基本（成本拆分）车辆部门编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap_b-vbdef17".equals(attribute)) {
			// 部门/楼层-合同基本（成本拆分）
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2)  and ((pk_group = '000112100000000005FD') and pk_defdoclist = (select l.pk_defdoclist from bd_defdoclist l where l.code = 'lcbm'))";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表体合同基本（成本拆分）部门/楼层编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体合同基本（成本拆分）部门/楼层编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("fct_ap_b-vbdef18".equals(attribute)) {
			// 用款人-合同基本（成本拆分）
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
					throw new BusinessException("表体合同基本（成本拆分）用款人编码" + code
							+ "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表体合同基本（成本拆分）用款人编码" + code
						+ "未能在NC档案中关联");
			}

		}
		// 添加币种2019-10-25-tzj
		else if ("fct_ap-corigcurrencyid".equals(attribute)) {
			// 币种
			sql = "select PK_CURRTYPE name from bd_currtype b where b.pk_currtype = ? and dr = 0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("表头币种" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("表头币种" + code + "未能在NC档案中关联");
			}

		}

		return pkValue;
	}

}
