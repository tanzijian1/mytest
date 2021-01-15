package nc.ui.tg.fischemepushstandard.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IFischemepushstandardMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceFischemepushstandardMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IFischemepushstandardMaintain query = NCLocator.getInstance().lookup(
				IFischemepushstandardMaintain.class);
		return query.query(queryScheme);
	}

}