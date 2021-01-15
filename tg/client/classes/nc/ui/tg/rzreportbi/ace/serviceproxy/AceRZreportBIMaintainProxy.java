package nc.ui.tg.rzreportbi.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IRZreportBIMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceRZreportBIMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IRZreportBIMaintain query = NCLocator.getInstance().lookup(
				IRZreportBIMaintain.class);
		return query.query(queryScheme);
	}

}