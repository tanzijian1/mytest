package nc.ui.tg.transferbill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITransferBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceTransferBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITransferBillMaintain query = NCLocator.getInstance().lookup(
				ITransferBillMaintain.class);
		return query.query(queryScheme);
	}

}