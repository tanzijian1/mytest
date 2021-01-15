package nc.ui.tg.contractapportionment.ace.handler;

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
			UFDouble def3 = new UFDouble(0);
			UFDouble def4 = new UFDouble(0);
			UFDouble def5 = new UFDouble(0);
			for(int i=0 ; i<row ; i++){
				UFDouble def1 = new UFDouble(0);//��˰���
				def3 = new UFDouble((String)panel.getBodyValueAt(i, "def3"));//һ�ڷ�̯���
				def4 = new UFDouble((String)panel.getBodyValueAt(i, "def4"));//���ڷ�̯���
				def5 = new UFDouble((String)panel.getBodyValueAt(i, "def5"));//���ڷ�̯���
				if("".equals(def1)||def1==null){
					def1 = new UFDouble(0);
					total = total.add(def1);
				}else{
					def1 = def1.add(def3).add(def4).add(def5);
					total = total.add(def1);
					panel.setBodyValueAt(def1, i, "def1");
				}
			}
		
			panel.setHeadItem("def11", new UFDouble(total));
	}

}
