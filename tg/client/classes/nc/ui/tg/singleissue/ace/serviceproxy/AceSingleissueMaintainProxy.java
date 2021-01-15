package nc.ui.tg.singleissue.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ISingleissueMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceSingleissueMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ISingleissueMaintain query = NCLocator.getInstance().lookup(
				ISingleissueMaintain.class);
		return query.query(queryScheme);
	}

}