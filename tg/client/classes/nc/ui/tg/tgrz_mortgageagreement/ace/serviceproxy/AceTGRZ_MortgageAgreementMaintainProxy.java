package nc.ui.tg.tgrz_mortgageagreement.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceTGRZ_MortgageAgreementMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ITGRZ_MortgageAgreementMaintain query = NCLocator.getInstance().lookup(
				ITGRZ_MortgageAgreementMaintain.class);
		return query.query(queryScheme);
	}

}