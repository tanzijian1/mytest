/**
 * <p>Title: AceCardBodyAfterEditEvent.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月3日 上午10:43:43

 * @version 1.0
 */

package nc.ui.tg.abnormaltaxtransfer.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;

/**
 * 创建时间：2019年9月3日 上午10:43:43  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.7
 * 文件名称：AceCardBodyAfterEditEvent.java  
 * 类说明：  
 */

/**
 * <p>
 * Title: AceCardBodyAfterEditEvent<／p>
 * 
 * <p>
 * Description: <／p>
 * 
 * <p>
 * Company: hanzhi<／p>
 * 
 * @author bobo
 * 
 * @date 2019年9月3日 上午10:43:43
 */

public class AceCardBodyAfterEditEvent implements
		IAppEventHandler<CardBodyAfterEditEvent> {

	/*
	 * (non-Javadoc) <p>Title: handleAppEvent<／p> <p>Description: <／p>
	 * 
	 * @param e
	 * 
	 * @see
	 * nc.ui.pubapp.uif2app.event.IAppEventHandler#handleAppEvent(nc.ui.uif2
	 * .AppEvent)
	 */
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		BillCardPanel panel = billForm.getBillCardPanel();
		int rowCount = panel.getRowCount();
		// 转出税额合计
		Double sum = new Double(0);
		for (int row = 0; row < rowCount; row++) {
			// 转出税额
			Object tax = panel.getBodyValueAt(row, "def3");
			if (tax != null) {

				if (!"".equals(tax.toString())) {
					Double tax_d = new Double(tax.toString());
					sum = sum + tax_d;
				}
			}
		}

		panel.setHeadItem("def11", sum.toString());

	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

}
