package nc.ui.tg.temporaryestimate.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.tg.temporaryestimate.dlg.FileUrlListDlg;
import nc.ui.uif2.NCAction;

public class LinkSRMEnclosure extends NCAction {
	FileUrlListDlg dlg = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -6505629760911368303L;

	private BillManageModel model = null;

	public LinkSRMEnclosure() {
		setCode("linkSRMEnclosure");
		setBtnName("Áª²ésrm¸½¼þ");
	}

	@Override
	public void doAction(ActionEvent arg0) throws Exception {
		String key = ((nc.vo.tgfn.temporaryestimate.AggTemest) getModel()
				.getSelectedData()).getParentVO().getDef8();
		dlg = new FileUrlListDlg(key);
		dlg.showModal();

	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
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
