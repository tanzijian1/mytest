package nc.ui.tg.standard.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.standard.StandardVO;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	@Override
	public void handleAppEvent(AddEvent e) {
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
		// 设置主组织默认值
		panel.setHeadItem(StandardVO.PK_GROUP, pk_group);
		panel.setHeadItem(StandardVO.PK_ORG, pk_org);
		panel.setHeadItem(StandardVO.PERIODYEAR, String.valueOf(AppContext
				.getInstance().getBusiDate().getYear()));

	}
}
