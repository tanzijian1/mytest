package nc.ui.tg.invoicing.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInvoicingMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceInvoicingMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IInvoicingMaintain query = NCLocator.getInstance().lookup(
				IInvoicingMaintain.class);
		return query.query(queryScheme);
	}

}