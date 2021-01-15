package nc.ui.tg.addticket.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

public class AceHeadBeforeEditHandler implements
IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		// TODO 自动生成的方法存根
		e.setReturnValue(true);
		if("def2".equals(e.getKey())){
			nc.ui.bd.ref.AbstractRefModel model=((UIRefPane)e.getBillCardPanel().getHeadItem("def2").getComponent()).getRefModel();
				 if(model instanceof nc.ui.cdm.contract.ref.ContractRefModel){
				 ((nc.ui.cdm.contract.ref.ContractRefModel)model).setAddTicketPk_org((String)e.getBillCardPanel().getHeadItem("pk_org").getValueObject());
				 }
		}
		if("def36".equals(e.getKey())){
			nc.ui.bd.ref.AbstractRefModel model=((UIRefPane)e.getBillCardPanel().getHeadItem("def36").getComponent()).getRefModel();
			String wheresql="and pk_org ='"+(String)e.getBillCardPanel().getHeadItem("pk_org").getValueObject()+"'";
			model.addWherePart(wheresql);
		}
	}

}
