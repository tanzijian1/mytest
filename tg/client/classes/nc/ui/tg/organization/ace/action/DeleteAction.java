package nc.ui.tg.organization.ace.action;


public class DeleteAction extends
		nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction {
	@Override
	protected boolean isActionEnable() {
		boolean isEnable = this.model.getSelectedData() != null;
		if (!isEnable) {
			return false;
		}
		return true;
	}
}
