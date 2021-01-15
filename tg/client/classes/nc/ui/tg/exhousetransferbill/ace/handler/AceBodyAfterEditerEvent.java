package nc.ui.tg.exhousetransferbill.ace.handler;

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
			UFDouble def8 = new UFDouble((String)panel.getBodyValueAt(i, "def8"));//含税金额
			if("".equals(def8)||def8==null){
				def8 = new UFDouble(0);
				total = total.add(def8);
			}else{
				total =total.add(def8);
			}
		}
		panel.setHeadItem("def11", total);
	}
}
