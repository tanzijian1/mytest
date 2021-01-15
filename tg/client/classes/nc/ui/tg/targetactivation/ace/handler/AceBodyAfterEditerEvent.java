package nc.ui.tg.targetactivation.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.vo.pub.lang.UFDouble;

public class AceBodyAfterEditerEvent implements IAppEventHandler<CardBodyAfterEditEvent>{

	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		// TODO 自动生成的方法存根
		BillCardPanel panel = e.getBillCardPanel();
		int row = panel.getRowCount();//获取表体行数
		UFDouble total = new UFDouble(0);
		for(int i=0 ; i<row ; i++){
			UFDouble def13 = new UFDouble((String)panel.getBodyValueAt(i, "def13"));//含税金额
			if("".equals(def13)||def13==null){
				def13 = new UFDouble(0);
				total = total.add(def13);
			}else{
				total = total.add(def13);
			}
		}
		panel.setHeadItem("def11", total);
	}
}
