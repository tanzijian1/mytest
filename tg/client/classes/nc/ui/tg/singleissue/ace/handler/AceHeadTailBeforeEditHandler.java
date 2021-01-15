package nc.ui.tg.singleissue.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;

public class AceHeadTailBeforeEditHandler implements IAppEventHandler<CardHeadTailBeforeEditEvent>{

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		if("def1".equals(e.getKey())){
			String pk_org = (String)e.getBillCardPanel().getHeadItem("pk_org").getValueObject();
			UIRefPane refpane = (UIRefPane)e.getBillCardPanel().getHeadItem(e.getKey()).getComponent();
			refpane.getRefModel().addWherePart("and nvl(def16,'N') <> 'Y' and pk_org='"+pk_org+"'");
		}
		e.setReturnValue(true);
	}

}
