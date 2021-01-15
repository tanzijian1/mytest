package nc.ui.tg.taxcalculation.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;

public class TaxHeadTailHandler implements IAppEventHandler<CardHeadTailAfterEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		// TODO 自动生成的方法存根
		 
		if("def23".equals(e.getKey())){
			//表头项目
			 String project=(String)e.getBillCardPanel().getHeadItem("def23").getValueObject();
		     int row=e.getBillCardPanel().getRowCount();
		     for(int i=0;i<row;i++){
		    	 e.getBillCardPanel().getBillModel("id_taxcalculationbody").setValueAt(null, i, "def1");
		    	 e.getBillCardPanel().getBillModel("id_taxcalculationbody").setValueAt(project, i, "def1");//表体项目
		     }
		}
	
	}

}
