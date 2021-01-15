package nc.ui.tg.invoicebill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInvoicebillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceInvoicebillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IInvoicebillMaintain query = NCLocator.getInstance().lookup(
				IInvoicebillMaintain.class);
		return query.query(queryScheme);
	}

}