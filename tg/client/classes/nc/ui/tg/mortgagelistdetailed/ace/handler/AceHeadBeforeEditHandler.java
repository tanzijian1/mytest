package nc.ui.tg.mortgagelistdetailed.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.tg.mortgagelist.MortgageListDetailedVO;

public class AceHeadBeforeEditHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent event) {
		// TODO 自动生成的方法存根

		if (MortgageListDetailedVO.PK_PERIODIZATION.equals(event.getKey())) {
			String pk_projectdata = (String) event.getBillCardPanel()
					.getHeadItem(MortgageListDetailedVO.PK_PROJECT)
					.getValueObject();
			UIRefPane refPane = (UIRefPane) event.getBillCardPanel()
					.getHeadItem(event.getKey()).getComponent();
			if (refPane.getRefPK() != null) {
				String wheresql = "and pk_projectdata = '" + pk_projectdata
						+ "'";
				refPane.getRefModel().addWherePart(wheresql);
			}

		}
		event.setReturnValue(true);

	}

}
