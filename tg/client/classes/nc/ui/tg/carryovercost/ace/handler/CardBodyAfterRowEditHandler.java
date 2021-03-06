/**
 * <p>Title: CardBodyAfterRowEditEvent.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月19日 下午11:29:45

 * @version 1.0
 */

package nc.ui.tg.carryovercost.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;

/**
 * 创建时间：2019年9月19日 下午11:29:45  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：CardBodyAfterRowEditEvent.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: CardBodyAfterRowEditEvent<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月19日 下午11:29:45
 */

public class CardBodyAfterRowEditHandler implements
		IAppEventHandler<CardBodyAfterRowEditEvent> {
	//表体行改变后，金额合计重新计算
	@Override
	public void handleAppEvent(CardBodyAfterRowEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		int rowCount = panel.getRowCount();
		Double sum = new Double(0);// 合计
		for (int row = 0; row < rowCount; row++) {
			// 金额
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
