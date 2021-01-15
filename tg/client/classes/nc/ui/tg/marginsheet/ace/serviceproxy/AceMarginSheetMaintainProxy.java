package nc.ui.tg.marginsheet.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMarginSheetMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceMarginSheetMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMarginSheetMaintain query = NCLocator.getInstance().lookup(
				IMarginSheetMaintain.class);
		return query.query(queryScheme);
	}

}