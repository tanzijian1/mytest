package nc.ui.tg.pub.filter;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.query2.refedit.IRefFilter;

public class BilltypeFilter implements IRefFilter {

	@Override
	public void doFilter(UIRefPane refPane) {

		refPane.getRefModel().addWherePart(
				"and parentbilltype in "
						+ "('261X','262X','263X','264X','265X')");

	}

}
