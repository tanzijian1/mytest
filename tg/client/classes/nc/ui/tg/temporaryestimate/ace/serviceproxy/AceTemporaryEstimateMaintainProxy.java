package nc.ui.tg.temporaryestimate.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITemporaryEstimateMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceTemporaryEstimateMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITemporaryEstimateMaintain query = NCLocator.getInstance().lookup(
				ITemporaryEstimateMaintain.class);
		return query.query(queryScheme);
	}

}