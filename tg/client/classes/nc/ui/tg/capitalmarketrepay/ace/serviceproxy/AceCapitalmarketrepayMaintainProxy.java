package nc.ui.tg.capitalmarketrepay.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceCapitalmarketrepayMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ICapitalmarketrepayMaintain query = NCLocator.getInstance().lookup(
				ICapitalmarketrepayMaintain.class);
		return query.query(queryScheme);
	}

}