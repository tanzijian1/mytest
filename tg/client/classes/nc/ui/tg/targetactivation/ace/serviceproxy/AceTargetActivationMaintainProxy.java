package nc.ui.tg.targetactivation.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITargetActivationMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceTargetActivationMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITargetActivationMaintain query = NCLocator.getInstance().lookup(
				ITargetActivationMaintain.class);
		return query.query(queryScheme);
	}

}