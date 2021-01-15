package nc.ui.tg.organization.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IOrganizationMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceOrganizationMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IOrganizationMaintain query = NCLocator.getInstance().lookup(
				IOrganizationMaintain.class);
		return query.query(queryScheme);
	}

}