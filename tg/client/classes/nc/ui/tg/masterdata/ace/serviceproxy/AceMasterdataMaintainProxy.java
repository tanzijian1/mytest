package nc.ui.tg.masterdata.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMasterdataMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceMasterdataMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMasterdataMaintain query = NCLocator.getInstance().lookup(
				IMasterdataMaintain.class);
		return query.query(queryScheme);
	}

}