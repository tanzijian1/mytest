package nc.ui.tg.standard.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.vo.tg.standard.StandardVO;

public class AceCardHeadTailBeforeEditEvent implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		e.setReturnValue(Boolean.TRUE);
		if (StandardVO.PK_FINTYPE.equals(e.getKey())) {
			BillItem item = (BillItem) e.getSource();
			UIRefPane refPane = (UIRefPane) item.getComponent();
			refPane.setWhereString("  (name in('前端融资','收并购融资','开发贷款'))");

		}
	}

}
