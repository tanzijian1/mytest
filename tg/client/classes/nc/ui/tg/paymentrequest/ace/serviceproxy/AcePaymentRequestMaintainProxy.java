package nc.ui.tg.paymentrequest.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IPaymentRequestMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AcePaymentRequestMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IPaymentRequestMaintain query = NCLocator.getInstance().lookup(
				IPaymentRequestMaintain.class);
		return query.query(queryScheme);
	}

}