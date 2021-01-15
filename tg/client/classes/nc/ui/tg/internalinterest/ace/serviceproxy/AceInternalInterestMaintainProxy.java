package nc.ui.tg.internalinterest.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInternalInterestMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceInternalInterestMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IInternalInterestMaintain query = NCLocator.getInstance().lookup(
				IInternalInterestMaintain.class);
		return query.query(queryScheme);
	}

}