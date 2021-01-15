package nc.ui.tg.addticket.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;

public class AceCardBodyBeforeEditEvent implements
		IAppEventHandler<CardBodyBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		if ("def1".equals(e.getKey())) {// 请款单号
			String pk_contract = ((UIRefPane) e.getBillCardPanel()
					.getHeadItem("def2").getComponent()).getRefPK();
			String pk_singleissue = ((UIRefPane) e.getBillCardPanel()
					.getHeadItem("def36").getComponent()).getRefPK();
			UIRefPane refPane = (UIRefPane) e.getBillCardPanel()
					.getBodyItem(e.getKey()).getComponent();
			refPane.setWhereString("  contractnum ='" + pk_contract + "'");
			//单期发行不为空，则重新设置参照过滤条件
			if(pk_singleissue!=null && pk_contract==null){
				refPane.setWhereString("  contractnum ='" + pk_singleissue + "'");
			}
		}

		e.setReturnValue(true);

	}

}
