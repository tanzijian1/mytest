package nc.ui.tg.fischeme.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IFischemeMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceFischemeMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IFischemeMaintain query = NCLocator.getInstance().lookup(
				IFischemeMaintain.class);
		return query.query(queryScheme);
	}

}