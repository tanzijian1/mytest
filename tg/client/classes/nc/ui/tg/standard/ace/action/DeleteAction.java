package nc.ui.tg.standard.ace.action;

import nc.ui.uif2.UIState;

public class DeleteAction extends
		nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction {
	@Override
	protected boolean isActionEnable() {
		boolean isEnable = this.model.getUiState() == UIState.NOT_EDIT
				&& this.model.getSelectedData() != null;
		if (!isEnable) {
			return false;
		}
		return true;
	}
}
