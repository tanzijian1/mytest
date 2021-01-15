package nc.ui.tg.pub.initializer;

import nc.ui.pubapp.uif2app.query2.IQueryConditionDLGInitializer;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.tg.pub.filter.MainOrgWithPermissionOrgFilter;

public class QryCondDLGInitializer implements IQueryConditionDLGInitializer {
	private nc.ui.pubapp.uif2app.model.BillManageModel model;

	@Override
	public void initQueryConditionDLG(
			QueryConditionDLGDelegator condDLGDelegator) {
		String orgName = "pk_org";
		// 设置主组织参照只包含用户有权限的组织
		condDLGDelegator.setRefFilter(orgName,
				new MainOrgWithPermissionOrgFilter(model.getContext()
						.getFuncInfo()));

	}

	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}

	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		this.model = model;
	}

}
