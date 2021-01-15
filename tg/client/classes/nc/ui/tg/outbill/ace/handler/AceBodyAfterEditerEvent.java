package nc.ui.tg.outbill.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);
		for (int row = 0; row < rowCount; row++) {
			Object tax = panel.getBodyValueAt(row, "money_de");
			if (tax != null) {

				if (!"".equals(tax.toString())) {
					Double tax_d = new Double(tax.toString());
					sum = sum + tax_d;
				}
			}
		}

		panel.setHeadItem("def11", sum.toString());
	}

}
