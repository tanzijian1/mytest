package nc.ui.tg.abnormaltaxtransfer.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IAbnormalTaxTransferMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceAbnormalTaxTransferMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IAbnormalTaxTransferMaintain query = NCLocator.getInstance().lookup(
				IAbnormalTaxTransferMaintain.class);
		return query.query(queryScheme);
	}

}