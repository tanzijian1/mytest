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
	 * ����id��ȡҵ�񵥾����ȼ�
	 * 
	 * @since 2020-02-19
	 * @author ̸�ӽ�
	 * @param tabblename
	 *            ҵ�����
	 * @param selectkey
	 *            ά�����ȼ��ֶ�
	 * @param primarykey
	 *            ҵ��������ֶ���
	 * @param billid
	 *            ҵ�������
	 * @return ���ȼ�
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
			 * ����֧��----Valueֵ��InstantlyPay----- 10 ��
			 * 1-2����-----Valueֵ��Within1to2days----- 5 ��ͨ
			 * ��ֹ���ڣ�������-----Valueֵ��Deadline----- -1��
			 */
			if (code != null) {
				// ����ִ��
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
