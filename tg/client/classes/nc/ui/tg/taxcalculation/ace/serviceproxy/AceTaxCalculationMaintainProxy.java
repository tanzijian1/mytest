package nc.ui.tg.taxcalculation.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITaxCalculationMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceTaxCalculationMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITaxCalculationMaintain query = NCLocator.getInstance().lookup(
				ITaxCalculationMaintain.class);
		return query.query(queryScheme);
	}

}