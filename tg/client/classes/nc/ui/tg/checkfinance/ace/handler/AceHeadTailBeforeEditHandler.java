package nc.ui.tg.checkfinance.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

public class AceHeadTailBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		// TODO 自动生成的方法存根
		if ("def1".equals(e.getKey())) {
			// 考核融资金额计算标准这里只控制查询开发贷,前端融资和收并购
			String wheresql = " and code in ('0001','0004','0005') ";
			UIRefPane ref = (UIRefPane) e.getBillCardPanel()
					.getHeadTailItem(e.getKey()).getComponent();
			ref.getRefModel().addWherePart(wheresql);
		} else if ("def5".equals(e.getKey())) {
			UIRefPane ref = (UIRefPane) e.getBillCardPanel()
					.getHeadTailItem(e.getKey()).getComponent();
			ref.setWhereString(getOrgFilterSQL());

		}
		e.setReturnValue(true);
	}

	/**
	 * 区域权限
	 * 
	 * @return
	 */
	public String getOrgFilterSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" pk_financeorg  in (")
				.append("select distinct v1.pk_financeorg from(  ")
				.append(getFinanceOrgSQL() + ")v1 ")
				.append(" left join ( ")
				.append(getFinanceOrgSQL()
						+ ")v2 on v1.pk_financeorg = v2.pk_fatherorg ")
				.append(" where v2.pk_financeorg is not null ").append(")");
		return sql.toString();

	}

	public String getFinanceOrgSQL() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT org_financeorg.pk_financeorg ")
				.append(",org_financeorg.code ")
				.append(",org_financeorg.name")
				.append(",org_orgs.pk_fatherorg")
				.append(",org_financeorg.enablestate ")
				.append(" FROM org_financeorg ")
				.append(" inner JOIN org_orgs  on org_financeorg.pk_financeorg = org_orgs.pk_org ")
				.append("  WHERE org_financeorg.enablestate = 2 and  org_orgs.enablestate = 2  order by org_orgs.code  ");
		return sql.toString();
	}
}
