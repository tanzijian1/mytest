package nc.bs.tg.pf.messagepriority;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.IMessagePriorityCallback;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;

public class MessagepriorityBase implements IMessagePriorityCallback {
	BaseDAO baseDAO = null;

	@Override
	public Integer getMessagePriority(String billid) throws BusinessException {
		return null;
	}

	/**
	 * 根据id获取业务单据优先级
	 * 
	 * @since 2020-02-19
	 * @author 谈子健
	 * @param tabblename
	 *            业务表名
	 * @param selectkey
	 *            维护优先级字段
	 * @param primarykey
	 *            业务表主键字段名
	 * @param billid
	 *            业务表主键
	 * @return 优先级
	 * @throws BusinessException
	 */
	protected String getPriority(String tabblename, String selectkey,
			String primarykey, String billid) throws BusinessException {
		String level = "-1";
		StringBuffer query = new StringBuffer();
		String sql = "select " + selectkey + " from " + tabblename + " where  "
				+ primarykey + "='" + billid + "'";
		String priority = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		if (priority != null) {

			query.append("select c.code  ");
			query.append("  from bd_defdoclist d  ");
			query.append("  left join bd_defdoc c  ");
			query.append("    on d.pk_defdoclist = c.pk_defdoclist  ");
			query.append(" where d.code = 'zdy031'  ");
			query.append(" and c.enablestate = 2  ");
			query.append(" and c.dr = 0  ");
			query.append(" and d.dr = 0  ");
			query.append(" and c.pk_defdoc = '" + priority + "'  ");
			String code = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			/**
			 * 即刻支付----Value值：InstantlyPay----- 10 高
			 * 1-2天内-----Value值：Within1to2days----- 5 普通
			 * 截止日期（待定）-----Value值：Deadline----- -1低
			 */
			if (code != null) {
				// 立即执行
				if ("InstantlyPay".equals(code)) {
					level = "10";
				}
				if ("Within1to2days".equals(code)) {
					level = "5";
				}
				if ("Deadline".equals(code)) {
					level = "-1";
				}
			}
		}
		return level;
	}

	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

}
