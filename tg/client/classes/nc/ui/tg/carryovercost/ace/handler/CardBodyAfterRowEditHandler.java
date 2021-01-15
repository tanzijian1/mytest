/**
 * <p>Title: CardBodyAfterRowEditEvent.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��19�� ����11:29:45

 * @version 1.0
 */

package nc.ui.tg.carryovercost.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;

/**
 * ����ʱ�䣺2019��9��19�� ����11:29:45  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * �ļ����ƣ�CardBodyAfterRowEditEvent.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: CardBodyAfterRowEditEvent<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��19�� ����11:29:45
 */

public class CardBodyAfterRowEditHandler implements
		IAppEventHandler<CardBodyAfterRowEditEvent> {
	//�����иı�󣬽��ϼ����¼���
	@Override
	public void handleAppEvent(CardBodyAfterRowEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);// �ϼ�
		for (int row = 0; row < rowCount; row++) {
			// ���
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
