package nc.ui.tg.pub.filter;

import nc.ui.pf.pub.BillTransTypeRefTreeModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;
import nc.vo.uap.rbac.FuncSubInfo;


public class CreditReferToFilter implements IRefFilter{


	public CreditReferToFilter(FuncSubInfo info) {
		super();
	}

	public void doFilter(UIRefPane refPane) {
				StringBuffer sql = new StringBuffer();
				sql.append(" and bd_billtype.pk_billtypecode in('RZ30','36FF','RZ06-Cxx-002','RZ06-Cxx-001') ");
				((BillTransTypeRefTreeModel)refPane.getRefModel()).setClassWherePart("systypecode in (select distinct bd_billtype.systemcode from bd_billtype where pk_billtypecode in('RZ30','36FF','RZ06-Cxx-002','RZ06-Cxx-001'))");
				refPane.getRefModel().addWherePart(sql.toString());
	}
}
