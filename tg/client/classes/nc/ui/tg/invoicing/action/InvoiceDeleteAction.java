package nc.ui.tg.invoicing.action;

import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

public class InvoiceDeleteAction extends
		nc.ui.pubapp.uif2app.actions.pflow.DeleteScriptAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3527175405341490404L;

	protected boolean isActionEnable() {

		boolean isEnable = super.isActionEnable();

		IBill bill = (IBill) getModel().getSelectedData();
		if (bill != null) {
			AggInvoicingHead aggvo = (AggInvoicingHead) bill;

			if ("Y".equals(aggvo.getParentVO().getDef47())) {
				isEnable = false;
			}
		}
		return isEnable;

	}

}
