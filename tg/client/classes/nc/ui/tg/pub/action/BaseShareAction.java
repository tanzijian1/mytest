package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BatchBillTableModel;
import nc.ui.uif2.NCAction;

public class BaseShareAction extends NCAction {
	BatchBillTableModel model = null;

	@Override
	public void doAction(ActionEvent e) throws Exception {

	}

	public BatchBillTableModel getModel() {
		return model;
	}

	public void setModel(BatchBillTableModel model) {
		model.addAppEventListener(this);
		this.model = model;
	}

	protected boolean isActionEnable() {
		if (getModel().getSelectedData() == null) {
			return false;
		}
		return true;
	}
}
