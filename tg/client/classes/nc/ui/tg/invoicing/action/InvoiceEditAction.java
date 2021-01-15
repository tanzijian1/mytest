package nc.ui.tg.invoicing.action;

import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

public class InvoiceEditAction extends nc.ui.pubapp.uif2app.actions.EditAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 94157308260895628L;

	public boolean isActionEnable() {

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
