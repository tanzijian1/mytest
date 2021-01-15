package nc.ui.tg.exhousetransferbill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IExHouseTransferBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceExHouseTransferBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IExHouseTransferBillMaintain query = NCLocator.getInstance().lookup(
				IExHouseTransferBillMaintain.class);
		return query.query(queryScheme);
	}

}