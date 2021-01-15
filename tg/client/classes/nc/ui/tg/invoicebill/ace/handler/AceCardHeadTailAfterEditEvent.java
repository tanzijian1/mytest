/**
 * <p>Title: AceCardHeadTailAfterEditEvent.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��3�� ����10:42:51

 * @version 1.0
 */

package nc.ui.tg.invoicebill.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;

/**
 * ����ʱ�䣺2019��9��3�� ����10:42:51  
 * ��Ŀ���ƣ�TG  
 * @author bobo
 * @version 1.0   
 * @since  JDK1.8
 * �ļ����ƣ�AceCardHeadTailAfterEditEvent.java  
 * ��˵����  
 */

/**
 * <p>
 * Title: AceCardHeadTailAfterEditEvent<��p>
 * 
 * <p>
 * Description: <��p>
 * 
 * <p>
 * Company: hanzhi<��p>
 * 
 * @author bobo
 * 
 * @date 2019��9��3�� ����10:42:51
 */

public class AceCardHeadTailAfterEditEvent implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	/*
	 * (non-Javadoc) <p>Title: handleAppEvent<��p> <p>Description: <��p>
	 * 
	 * @param e
	 * 
	 * @see
	 * nc.ui.pubapp.uif2app.event.IAppEventHandler#handleAppEvent(nc.ui.uif2
	 * .AppEvent)
	 */
	nc.ui.pubapp.uif2app.view.ShowUpableBillForm billForm;

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		BillCardPanel panel = billForm.getBillCardPanel();
		BillItem hSupplier = panel.getHeadItem("supplier");
		Object supplierValue = hSupplier.getValueObject();
		if (supplierValue != null) {
			int rowcout = panel.getRowCount();
			for (int row = 0; row < rowcout; row++) {
				panel.setBodyValueAt(supplierValue, row, "supplier");
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
