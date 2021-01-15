package nc.ui.tg.taxcalculation.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.lang.UFDouble;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO 自动生成的方法存根
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);
		for (int row = 0; row < rowCount; row++) {
			Object tax = panel.getBodyValueAt(row, "def14");
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
