package nc.ui.tg.contractapportionment.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IContractApportionmentMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceContractApportionmentMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IContractApportionmentMaintain query = NCLocator.getInstance().lookup(
				IContractApportionmentMaintain.class);
		return query.query(queryScheme);
	}

}