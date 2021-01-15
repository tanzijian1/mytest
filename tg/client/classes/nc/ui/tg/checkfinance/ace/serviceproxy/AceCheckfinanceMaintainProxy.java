package nc.ui.tg.checkfinance.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ICheckfinanceMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceCheckfinanceMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ICheckfinanceMaintain query = NCLocator.getInstance().lookup(
				ICheckfinanceMaintain.class);
		return query.query(queryScheme);
	}

}