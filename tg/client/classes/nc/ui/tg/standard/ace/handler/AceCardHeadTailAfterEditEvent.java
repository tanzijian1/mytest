package nc.ui.tg.standard.ace.handler;

import javax.swing.Action;
import javax.swing.ActionMap;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.tg.standard.StandardBVO;
import nc.vo.tg.standard.StandardVO;

public class AceCardHeadTailAfterEditEvent implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		if (StandardVO.PK_FINTYPE.equals(e.getKey())) {
			UIRefPane refPanel = (UIRefPane) e.getSource();
			String typename = refPanel.getRefModel().getRefNameValue();
			if ("前端融资".equals(typename)) {
				e.getBillCardPanel().getBillModel().clearBodyData();
				e.getBillCardPanel().getHeadItem(StandardVO.DEF1)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF2)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF3)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF4)
						.setEdit(false);
				e.getBillCardPanel().setHeadItem(StandardVO.DEF1,
						UFBoolean.FALSE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF2,
						UFBoolean.FALSE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF3,
						UFBoolean.FALSE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF4,
						UFBoolean.FALSE.toString());
				e.getBillCardPanel().addLine();
				e.getBillCardPanel().addLine();
				e.getBillCardPanel().setBodyValueAt(1, 0, "def1");
				e.getBillCardPanel().setBodyValueAt(2, 1, "def1");
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setEdit(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setNull(false);
				/*e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setEdit(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setNull(false);*/
				ActionMap actionMap = e.getBillCardPanel().getActionMap();
				actionMap.get("增行").setEnabled(false);
				actionMap.get("插入行").setEnabled(false);
				actionMap.get("删行").setEnabled(false);

			} else if ("收并购融资".equals(typename)) {
				e.getBillCardPanel().getBillModel().clearBodyData();

				e.getBillCardPanel().setHeadItem(StandardVO.DEF1,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF2,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF3,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF4,
						UFBoolean.FALSE.toString());

				e.getBillCardPanel().getHeadItem(StandardVO.DEF1).setEdit(true);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF2).setEdit(true);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF3).setEdit(true);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF4).setEdit(true);

				e.getBillCardPanel().addLine();
				e.getBillCardPanel().setBodyValueAt(null, 0, "def1");
				e.getBillCardPanel().getBodyItem("def1").setEdit(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setEdit(true);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setNull(true);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setEdit(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setNull(false);
				ActionMap actionMap = e.getBillCardPanel().getActionMap();
				actionMap.get("增行").setEnabled(false);
				actionMap.get("插入行").setEnabled(false);
				actionMap.get("删行").setEnabled(false);

			} else if ("开发贷款".equals(typename)) {
				e.getBillCardPanel().getBillModel().clearBodyData();
				e.getBillCardPanel().getHeadItem(StandardVO.DEF1)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF2)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF3)
						.setEdit(false);
				e.getBillCardPanel().getHeadItem(StandardVO.DEF4)
						.setEdit(false);
				e.getBillCardPanel().setHeadItem(StandardVO.DEF1,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF2,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF3,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().setHeadItem(StandardVO.DEF4,
						UFBoolean.TRUE.toString());
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setEdit(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO1)
						.setNull(false);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setEdit(true);
				e.getBillCardPanel().getBodyItem(StandardBVO.RATIO2)
						.setNull(true);
				ActionMap actionMap = e.getBillCardPanel().getActionMap();
				actionMap.get("增行").setEnabled(true);
				actionMap.get("插入行").setEnabled(true);
				actionMap.get("删行").setEnabled(true);
			}

		}

	}
}
