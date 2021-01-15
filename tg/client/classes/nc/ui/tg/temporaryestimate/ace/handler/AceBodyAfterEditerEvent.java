package nc.ui.tg.temporaryestimate.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.lang.UFDouble;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO �Զ����ɵķ������
		BillCardPanel panel = e.getBillCardPanel();
		int row = panel.getRowCount();//��ȡ��������
		UFDouble total = new UFDouble(0);
		for(int i=0 ; i<row ; i++){
			UFDouble def2 = new UFDouble((String)panel.getBodyValueAt(i, "def2"));//��˰���
			if("".equals(def2)||def2==null){
				def2 = new UFDouble(0);
				total = total.add(def2);
			}else{
				total = total.add(def2);
			}
		}
		panel.setHeadItem("def11", total);
	}
}
