package nc.ui.tg.renamechangebill.ace.handler;

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
			UFDouble def10 = new UFDouble((String)panel.getBodyValueAt(i, "def10"));//��˰���
			if("".equals(def10)||def10==null){
				def10 = new UFDouble(0);
				total = total.add(def10);
			}else{
				total = total.add(def10);
			}
		}
		panel.setHeadItem("def11", total.toString());
	}

}
