/**
 * <p>Title: PayBillConvertor.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月16日 下午3:36:18

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
 * 创建时间：2019年9月16日 下午3:36:18  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：PayBillConvertor.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: PayBillConvertor<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月16日 下午3:36:18
 */

public class PayBillConvertor extends DefaultConvertor {

	/*
	 * (non-Javadoc) <p>Title: getRefAttributePk<／p> <p>Description: <／p>
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
		// 首个条件参数默认编码
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		BaseDAO dao = new BaseDAO();

		if ("paybill-supplier".equals(attribute)) {
			// 表头供应商（客户，客商）
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
					throw new BusinessException("财务组织编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("财务组织编码" + code + "未能在NC档案中关联");
			}
		} else if ("paybill-pk_tradetypeid".equals(attribute)) {
			// 付款类型
			sql = "select pk_billtypeid  from bd_billtype where "
					+ "pk_billtypecode = ? and nvl(dr,0) = 0 and pk_group = '000112100000000005FD'";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("付款类型" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("付款类型" + code + "未能在NC档案中关联");
			}
		} else if ("paybill-pk_currtype".equals(attribute)) {
			// 币种
			sql = "select pk_currtype from bd_currtype where "
					+ "code = ? and nvl(dr,0) = 0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("币种编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("币种编码" + code + "未能在NC档案中关联");
			}
		}

		else if ("payitem-pk_balatype".equals(attribute)) {
			// 结算方式
			sql = "select pk_balatype from bd_balatype  where "
					+ "(code = ? or name = ?) and ( isnull(directincome, 'N') = 'N' or  isnull(directincome, '~') = '~') and (enablestate = 2)";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("结算方式编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("结算方式编码" + code + "未能在NC档案中关联");
			}
		}

		else if ("payitem-payaccount".equals(attribute)) {
			// 付款银行账户
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
				 * if(pkValue == null){ throw new BusinessException("付款银行账户编码" +
				 * code + "未能在NC档案中关联"); }
				 */
			} catch (DAOException e) {
				// throw new BusinessException("付款银行账户编码" + code +
				// "未能在NC档案中关联");
			}
		}

		else if ("payitem-supplier".equals(attribute)) {
			// 表体供应商（客户，客商）
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
					throw new BusinessException("客户编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("客户编码" + code + "未能在NC档案中关联");
			}
		} else if ("payitem-recaccount".equals(attribute)) {
			// 客户银行账号
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
					throw new BusinessException("客户银行账号" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("客户银行账号" + code + "未能在NC档案中关联");
			}
		} else if ("payitem-def11".equals(attribute)) {
			// 收款银行账户开户行

			sql = "select pk_banktype from bd_banktype where "
					+ "code = ? and nvl(dr,0) = 0";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				// if(pkValue == null){
				// throw new BusinessException("收款银行账户开户行编码" + code +
				// "未能在NC档案中关联");
				// }
			} catch (DAOException e) {
				// throw new BusinessException("收款银行账户开户行编码" + code +
				// "未能在NC档案中关联");
			}
		} else if ("payitem-def21".equals(attribute)) {
			// 所属项目(NC房地产项目)
			sql = "select  bd_project.pk_project from bd_project bd_project where "
					+ "def2 = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
//				if (pkValue == null) {
//					throw new BusinessException("所属项目(NC房地产项目)编码" + code
//							+ "未能在NC档案中关联");
//				}
			} catch (DAOException e) {
				throw new BusinessException("所属项目(NC房地产项目)编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("payitem-def22".equals(attribute)) {
			// 购买房间（NC房地产项目明细）
			sql = "select bd_project.pk_project from bd_project bd_project where "
					+ "def2 = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
//				if (pkValue == null) {
//					throw new BusinessException("购买房间（NC房地产项目明细）编码" + code
//							+ "未能在NC档案中关联");
//				}
			} catch (DAOException e) {
				throw new BusinessException("购买房间（NC房地产项目明细）编码" + code
						+ "未能在NC档案中关联");
			}
		} else if ("payitem-def23".equals(attribute)) {
			// 款项类型
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("款项类型编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("款项类型编码" + code + "未能在NC档案中关联");
			}
		} else if ("payitem-def24".equals(attribute)) {
			// 款项名称
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "code = ?  and (enablestate = 2) and nvl(dr,0)=0 ";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("款项编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("款项编码" + code + "未能在NC档案中关联");
			}
		} else if ("payitem-def25".equals(attribute)) {
			// 业态
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "name = ?  and (enablestate = 2)  and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("业态编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("业态编码" + code + "未能在NC档案中关联");
			}
		} else if ("payitem-def26".equals(attribute)) {
			// 计税方式
			sql = "select pk_defdoc from bd_defdoc  where "
					+ "name = ? and (enablestate = 2) and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("计税方式编码" + code + "未能在NC档案中关联");
				}
			} catch (DAOException e) {
				throw new BusinessException("计税方式编码" + code + "未能在NC档案中关联");
			}
		}

		return pkValue;
	}

}
