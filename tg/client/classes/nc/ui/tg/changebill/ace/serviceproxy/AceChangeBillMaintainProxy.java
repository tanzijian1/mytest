package nc.ui.tg.changebill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IChangeBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceChangeBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IChangeBillMaintain query = NCLocator.getInstance().lookup(
				IChangeBillMaintain.class);
		return query.query(queryScheme);
	}

}