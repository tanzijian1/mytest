package nc.ui.tg.addticket.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IAddticketMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceAddticketMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IAddticketMaintain query = NCLocator.getInstance().lookup(
				IAddticketMaintain.class);
		return query.query(queryScheme);
	}

}