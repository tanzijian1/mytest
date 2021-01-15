package nc.ui.tg.financingplan.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IFinancingPlanMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceFinancingPlanMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IFinancingPlanMaintain query = NCLocator.getInstance().lookup(
				IFinancingPlanMaintain.class);
		return query.query(queryScheme);
	}

}