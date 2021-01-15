package nc.ui.tg.financingexpense.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pubapp.uif2app.actions.AbstractReferenceAction;
import nc.ui.uif2.NCAction;

public class FinancingExpensePullAction  extends AbstractReferenceAction{
	nc.ui.pubapp.uif2app.model.BillManageModel model;
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm  editor;
	
	public nc.ui.pubapp.uif2app.model.BillManageModel getModel() {
		return model;
	}
	public void setModel(nc.ui.pubapp.uif2app.model.BillManageModel model) {
		this.model = model;
	}
	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getEditor() {
		return editor;
	}
	public void setEditor(nc.ui.pubapp.uif2app.view.ShowUpableBillForm editor) {
		this.editor = editor;
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO 自动生成的方法存根
		PfUtilClient.childButtonClicked(getSourceBillType(), getModel()
				.getContext().getPk_group(), getModel().getContext()
				.getPk_loginUser(), "RZ06", getModel().getContext()
				.getEntranceUI(), null, null);
		if (PfUtilClient.isCloseOK()) {
//			AggOutputTaxVO[] vos = (AggOutputTaxVO[]) KPSQPFUtilCilent
//					.getRetVos();
//			this.getTransferViewProcessor().processBillTransfer(vos);
		}
	}

}
