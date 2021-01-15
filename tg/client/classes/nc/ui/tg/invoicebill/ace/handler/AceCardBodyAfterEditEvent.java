/**
 * <p>Title: AceCardBodyAfterEditEvent.java<／p>

 * <p>Description: <／p>

 * <p>Copyright: Copyright (c) 2019<／p>

 * <p>Company: hanzhi<／p>

 * @author bobo

 * @date 2019年9月3日 上午10:43:43

 * @version 1.0
 */

package nc.ui.tg.invoicebill.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.lang.UFDouble;

/**
 * 创建时间：2019年9月3日 上午10:43:43  
 * 项目名称：TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.8
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
		for (int row = 0; row < rowCount; row++) {
			// 数量
			Object numValue = panel.getBodyValueAt(row, "quantity_de");
			// 含税单价
			Object taxpriceValue = panel.getBodyValueAt(row, "taxprice");
			// 税率
			Object taxrateValue = panel.getBodyValueAt(row, "taxrate");

			if (numValue != null && taxpriceValue != null
					&& taxrateValue != null) {
				UFDouble quantityDe = new UFDouble(numValue.toString());
				UFDouble taxprice = new UFDouble(taxpriceValue.toString());
				UFDouble taxrate = new UFDouble(taxrateValue.toString());

				// 税额=含税单价/（1+税率）*税率*数量
				panel.setBodyValueAt(taxprice.getDouble()
						/ (1 + taxrate.getDouble()) * taxrate.getDouble()
						* quantityDe.getDouble(), row, "local_tax_de");

				// 不含税金额=含税单价/（1+税率）*数量
				panel.setBodyValueAt(taxprice.getDouble()
						/ (1 + taxrate.getDouble()) * quantityDe.getDouble(),
						row, "notax_de");
			}

			if (numValue != null && taxpriceValue != null) {
				UFDouble quantityDe = new UFDouble(numValue.toString());
				UFDouble taxprice = new UFDouble(taxpriceValue.toString());

				// 价税合计=含税单价*数量
				panel.setBodyValueAt(taxprice.getDouble()
						* quantityDe.getDouble(),
						row, "money_de");
			}
		}

	}

	public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(
			nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm) {
		this.billForm = billForm;
	}

}
