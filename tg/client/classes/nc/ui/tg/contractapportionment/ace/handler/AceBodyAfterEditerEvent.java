package nc.ui.tg.contractapportionment.ace.handler;

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
			UFDouble def3 = new UFDouble(0);
			UFDouble def4 = new UFDouble(0);
			UFDouble def5 = new UFDouble(0);
			for(int i=0 ; i<row ; i++){
				UFDouble def1 = new UFDouble(0);//含税金额
				def3 = new UFDouble((String)panel.getBodyValueAt(i, "def3"));//一期分摊金额
				def4 = new UFDouble((String)panel.getBodyValueAt(i, "def4"));//二期分摊金额
				def5 = new UFDouble((String)panel.getBodyValueAt(i, "def5"));//三期分摊金额
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
