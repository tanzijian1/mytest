package nc.ui.tg.carryovercost.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ICarryoverCostMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceCarryoverCostMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ICarryoverCostMaintain query = NCLocator.getInstance().lookup(
				ICarryoverCostMaintain.class);
		return query.query(queryScheme);
	}

}