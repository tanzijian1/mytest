package nc.ui.tg.moonfinancingplan.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

public class AceHeadBeforeEditHandler implements
IAppEventHandler<CardHeadTailBeforeEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent event) {
		// TODO 自动生成的方法存根
		
		if("pro_phases".equals(event.getKey())){
			nc.ui.pub.bill.BillItem item = (BillItem) event.getBillCardPanel().getHeadItem("project");
			UIRefPane refPane = (UIRefPane) item.getComponent();
			if(refPane.getRefPK() != null){
			String wheresql	= "and pk_projectdata = '" + refPane.getRefPK() + "'";
			((UIRefPane)((BillItem)event.getSource()).getComponent()).getRefModel().addWherePart(wheresql);
			}
			
		}
		event.setReturnValue(true);

	}


}
