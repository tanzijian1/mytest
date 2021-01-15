package nc.ui.tg.pub.action;

import nc.ui.pubapp.uif2app.model.BillManageModel;

public class RefreshAction extends
		nc.ui.pubapp.uif2app.query2.action.DefaultRefreshAction {
	@Override
	protected boolean isActionEnable() {
		boolean refreshable = false;
		if (((BillManageModel) getModel()).getSelectedOperaDatas() != null
				&& ((BillManageModel) getModel()).getSelectedOperaDatas().length > 0) {
			refreshable = true;
		}
		return refreshable;
	}
}
