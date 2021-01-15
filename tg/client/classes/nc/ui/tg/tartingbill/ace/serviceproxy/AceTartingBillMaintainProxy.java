package nc.ui.tg.tartingbill.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITartingBillMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * ʾ�����ݵĲ�������
 * 
 * @author author
 * @version tempProject version
 */
public class AceTartingBillMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITartingBillMaintain query = NCLocator.getInstance().lookup(
				ITartingBillMaintain.class);
		return query.query(queryScheme);
	}

}