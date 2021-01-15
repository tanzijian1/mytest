package nc.ui.tg.distribution.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IDistributionMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceDistributionMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IDistributionMaintain query = NCLocator.getInstance().lookup(
				IDistributionMaintain.class);
		return query.query(queryScheme);
	}

}