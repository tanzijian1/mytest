package nc.ui.tg.capitalmarketrepay.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.tg.singleissue.pub.ref.RepaymentPlanRefModel;

public class AceBodyBeforeEditHandler implements IAppEventHandler<CardBodyBeforeEditEvent>{

	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		// TODO 自动生成的方法存根
		String tabCode = e.getTableCode();
		if("pk_marketrepaley_b".equals(tabCode)){
			if("def1".equals(e.getKey())){
				String pk_singleissue = (String)e.getBillCardPanel().getHeadItem("def5").getValueObject();
				UIRefPane refPane = (UIRefPane)e.getBillCardPanel().getBodyItem(tabCode, "def1").getComponent();
				RepaymentPlanRefModel refModel = (RepaymentPlanRefModel)refPane.getRefModel();
				refModel.setPk_singleissue(pk_singleissue);
			}
		}
		
		e.setReturnValue(true);
	}

}
