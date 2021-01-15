package nc.ui.tg.standard.ace.handler;

import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;

public class AceCardBodyBeforeEditEvent implements
		IAppEventHandler<CardBodyBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		

		e.setReturnValue(Boolean.TRUE);
	}

}
