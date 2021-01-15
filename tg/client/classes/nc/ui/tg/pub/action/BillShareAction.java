package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;

public class BillShareAction extends NCAction {
	nc.ui.pubapp.uif2app.model.BillManageModel model = null;
	private ShowUpableBillForm editor;

	@Override
	public void doAction(ActionEvent e) throws Exception {

	}

	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}

	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	protected boolean isActionEnable() {
		if (getModel().getSelectedOperaDatas() == null
				|| getModel().getSelectedOperaDatas().length == 0) {
			return false;
		}
		return true;
	}

	public ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}

}
