package nc.ui.tg.marginworkorder.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.IMarginWorkorderMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceMarginWorkorderMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IMarginWorkorderMaintain query = NCLocator.getInstance().lookup(
				IMarginWorkorderMaintain.class);
		return query.query(queryScheme);
	}

}