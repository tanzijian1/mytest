package nc.ui.tg.fischeme.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;

public class AceBodyBeforeEditHandler implements
		IAppEventHandler<CardBodyBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		if(e.getRow()==0){
			e.setReturnValue(false);
		}else if(e.getRow()==2){
			e.setReturnValue(false);
		}else{
			e.setReturnValue(true);
		}

	}

}
