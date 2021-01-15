package nc.ui.tg.moonfinancingplan.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMoonFinancingPlanMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceMoonFinancingPlanMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMoonFinancingPlanMaintain query = NCLocator.getInstance().lookup(
				IMoonFinancingPlanMaintain.class);
		return query.query(queryScheme);
	}

}