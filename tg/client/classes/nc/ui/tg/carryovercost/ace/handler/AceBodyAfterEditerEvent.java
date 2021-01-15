package nc.ui.tg.carryovercost.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);// ºÏ¼Æ
		for (int row = 0; row < rowCount; row++) {
			// ½ð¶î
			Object tax_cr = panel.getBodyValueAt(row, "def9");
			
			if (tax_cr != null) {

				if (!"".equals(tax_cr.toString())) {
					Double tax_d = new Double(tax_cr.toString());
					sum = sum + tax_d;
				}
			}
			
		}
		panel.setHeadItem("def11", sum.toString());
	}
}

