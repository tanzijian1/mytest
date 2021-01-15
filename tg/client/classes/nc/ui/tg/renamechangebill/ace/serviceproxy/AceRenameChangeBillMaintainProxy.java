package nc.ui.tg.renamechangebill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IRenameChangeBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceRenameChangeBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IRenameChangeBillMaintain query = NCLocator.getInstance().lookup(
				IRenameChangeBillMaintain.class);
		return query.query(queryScheme);
	}

}