package nc.ui.tg.salaryfundaccure.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ISalaryfundaccureMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceSalaryfundaccureMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ISalaryfundaccureMaintain query = NCLocator.getInstance().lookup(
				ISalaryfundaccureMaintain.class);
		return query.query(queryScheme);
	}

}