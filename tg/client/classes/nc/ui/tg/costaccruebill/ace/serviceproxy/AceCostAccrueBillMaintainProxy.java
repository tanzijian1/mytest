package nc.ui.tg.costaccruebill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ICostAccrueBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceCostAccrueBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ICostAccrueBillMaintain query = NCLocator.getInstance().lookup(
				ICostAccrueBillMaintain.class);
		return query.query(queryScheme);
	}

}