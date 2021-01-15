
package nc.ui.tg.fischeme.ace.handler;

import java.util.ArrayList;
import java.util.List;

import org.geotools.resources.SwingUtilities;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.vo.pub.lang.UFDate;

public class AceBodyTailAfterEditHandler implements
IAppEventHandler<CardBodyAfterEditEvent> {
	
	private BillForm billForm;
	
	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		List<String> itmekey = new ArrayList<String>();
		itmekey.add("def1");
		itmekey.add("def2");
		itmekey.add("def3");
		itmekey.add("def4");
		itmekey.add("def5");
		itmekey.add("def6");
		itmekey.add("def7");
		itmekey.add("vbdef1");
		itmekey.add("vbdef2");
		itmekey.add("vbdef3");
		itmekey.add("vbdef4");
		itmekey.add("vbdef5");
		itmekey.add("vbdef6");
		itmekey.add("vbdef7");
		
		UFDate date1 = null;
		UFDate date2 = null;
		int line = 0;
		if (e.getRow() == 0 || e.getRow() == 1) {
			if (itmekey.contains(e.getKey())) {
				BillItem item = e.getBillCardPanel().getBodyItem(e.getKey());
				try {
					line = 1;
					if((String) e.getBillCardPanel()
							.getBodyValueAt(0, e.getKey())!=null){
						date1 = new UFDate((String) e.getBillCardPanel()
								.getBodyValueAt(0, e.getKey()));
					}else{
						date1 = null;
					}
					if (date1 != null) {
						new UFDate(date1.toString());
					}
					line = 2;
					if((String) e.getBillCardPanel()
							.getBodyValueAt(1, e.getKey())!=null){
						date2 = new UFDate((String) e.getBillCardPanel()
								.getBodyValueAt(1, e.getKey()));
					}else{
						date2 = null;
					}
					if (date2 != null) {
						new UFDate(date2.toString());
					}
				} catch (Exception e1) {
					SwingUtilities.showMessageDialog(e.getBillCardPanel(), "第"
							+ (line) + "行," + item.getName()
							+ "非[yyyy-mm-dd]日期格式 ,请重新调整!", "提示",
							MessageDialog.HINT_ICON);
					e.getBillCardPanel().setBodyValueAt(null, e.getRow(),
							e.getKey());
					e.getBillCardPanel().setBodyValueAt(null, 2,
							e.getKey());
					e1.printStackTrace();
				}finally{
//					e.getBillCardPanel().setBodyValueAt((String) e.getBillCardPanel()
//							.getBodyValueAt(e.getRow(), e.getKey()), e.getRow(),
//							e.getKey());
				}
			}
		}
		if(date1!=null&&date2!=null){
			e.getBillCardPanel().setBodyValueAt(new UFDate(date1.toString()).getDaysAfter(new UFDate(date2.toString())), 2,
					e.getKey());
		}
		
		if(date1==null||date2==null){
			e.getBillCardPanel().setBodyValueAt(null, 2,
					e.getKey());
		}
		
	}
	
	public BillForm getEditor() {
		return billForm;
	}
	
	public void setEditor(BillForm billForm) {
		this.billForm = billForm;
	}
	
}