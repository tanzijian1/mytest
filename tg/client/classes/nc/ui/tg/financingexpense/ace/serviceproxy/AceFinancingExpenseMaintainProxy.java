package nc.ui.tg.financingexpense.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceFinancingExpenseMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IFinancingExpenseMaintain query = NCLocator.getInstance().lookup(
				IFinancingExpenseMaintain.class);
		return query.query(queryScheme);
	}

}