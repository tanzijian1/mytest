package nc.ui.tg.pub.action;

import nc.ui.tg.pub.model.BaseTreeDataAppModel;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.CancelAction;

public class TreeCancelAction extends CancelAction {
	@Override
	protected void doBeforeCancel() {
		BaseTreeDataAppModel appModel = ((BaseTreeDataAppModel) getModel());
		if (appModel.getBillCodeContext() != null
				&& getModel().getUiState() == UIState.ADD)
			appModel.rollbackBillCode();

	}

}
