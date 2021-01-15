package nc.ui.tg.marginworkorder.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMarginWorkorderMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceMarginWorkorderMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMarginWorkorderMaintain query = NCLocator.getInstance().lookup(
				IMarginWorkorderMaintain.class);
		return query.query(queryScheme);
	}

}