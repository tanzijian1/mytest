package nc.ui.tg.outbill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IOutBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceOutBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IOutBillMaintain query = NCLocator.getInstance().lookup(
				IOutBillMaintain.class);
		return query.query(queryScheme);
	}

}