/**
 * <p>Title: CardBodyAfterRowEditEvent.java<��p>

 * <p>Description: <��p>

 * <p>Copyright: Copyright (c) 2019<��p>

 * <p>Company: hanzhi<��p>

 * @author bobo

 * @date 2019��9��19�� ����11:29:45

 * @version 1.0
 */

package nc.ui.tg.contractapportionment.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent;
import nc.vo.pub.lang.UFDouble;

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
