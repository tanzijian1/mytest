/**
 * <p>Title: CardBodyAfterRowEditEvent.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月19日 下午11:29:45

 * @version 1.0
 */

package nc.ui.tg.paymentrequest.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;


public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent> {
	//表体行改变后，金额合计重新计算

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);
		for (int row = 0; row < rowCount; row++) {
			Object tax = panel.getBodyValueAt(row, "local_money_de");
			if (tax != null) {

				if (!"".equals(tax.toString())) {
					Double tax_d = new Double(tax.toString());
					sum = sum + tax_d;
				}
			}
		}

		panel.setHeadItem("money", sum.toString());
		
	}

}
