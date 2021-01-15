package nc.ui.tg.tg_groupdata.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITG_GroupDataMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceTG_GroupDataMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITG_GroupDataMaintain query = NCLocator.getInstance().lookup(
				ITG_GroupDataMaintain.class);
		return query.query(queryScheme);
	}

}