package nc.ui.tg.approvalpro.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IApprovalproMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceApprovalproMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IApprovalproMaintain query = NCLocator.getInstance().lookup(
				IApprovalproMaintain.class);
		return query.query(queryScheme);
	}

}