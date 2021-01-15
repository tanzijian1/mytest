package nc.ui.tg.interestshare.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IInterestShareMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceInterestShareMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IInterestShareMaintain query = NCLocator.getInstance().lookup(
				IInterestShareMaintain.class);
		return query.query(queryScheme);
	}

}