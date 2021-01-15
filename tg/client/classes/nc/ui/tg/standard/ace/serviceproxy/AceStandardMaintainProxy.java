package nc.ui.tg.standard.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IStandardMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceStandardMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IStandardMaintain query = NCLocator.getInstance().lookup(
				IStandardMaintain.class);
		return query.query(queryScheme);
	}

}