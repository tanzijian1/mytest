package nc.ui.tg.fischemepushstandard.ace.handler;

import org.apache.commons.lang.StringUtils;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;

public class AceHeadTailAfterEditHandler implements
IAppEventHandler<CardHeadTailAfterEditEvent> {

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		if ("def1".equals(e.getKey())) {
			UIRefPane ref = (UIRefPane) e.getBillCardPanel()
					.getHeadTailItem(e.getKey()).getComponent();
			if(StringUtils.isBlank(ref.getRefCode())){
				nc.vo.pubapp.pattern.exception.ExceptionUtils
			      .wrappBusinessException("该融资机构类型未维护,请维护NC自定义档案");
			}
			//银行
			if ("001".equals(ref.getRefCode())) {
				e.getBillCardPanel().getBillModel("id_bvo")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_nvo")
						.clearBodyData();
				e.getBillCardPanel().getBillModel("id_cvo")
						.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_bvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_bvo").delLine(new int[e.getBillCardPanel().getBillModel("id_bvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_nvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_nvo").delLine(new int[e.getBillCardPanel().getBillModel("id_nvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_cvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_cvo").delLine(new int[e.getBillCardPanel().getBillModel("id_cvo").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_bvo").addLine();
				e.getBillCardPanel().getBillModel("id_bvo").setValueAt("计划完成", 0,
						"def13");
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
			}
			//非银
			if ("002".equals(ref.getRefCode())) {
				e.getBillCardPanel().getBillModel("id_bvo")
				.clearBodyData();
				e.getBillCardPanel().getBillModel("id_nvo")
				.clearBodyData();
				e.getBillCardPanel().getBillModel("id_cvo")
				.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_bvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_bvo").delLine(new int[e.getBillCardPanel().getBillModel("id_bvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_nvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_nvo").delLine(new int[e.getBillCardPanel().getBillModel("id_nvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_cvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_cvo").delLine(new int[e.getBillCardPanel().getBillModel("id_cvo").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_nvo").addLine();
				e.getBillCardPanel().getBillModel("id_nvo").setValueAt("计划完成", 0,
						"def13");
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(1);
			}
			//资本市场
			if ("003".equals(ref.getRefCode())) {
				e.getBillCardPanel().getBillModel("id_bvo")
				.clearBodyData();
				e.getBillCardPanel().getBillModel("id_nvo")
				.clearBodyData();
				e.getBillCardPanel().getBillModel("id_cvo")
				.clearBodyData();
				if(e.getBillCardPanel().getBillModel("id_bvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_bvo").delLine(new int[e.getBillCardPanel().getBillModel("id_bvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_nvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_nvo").delLine(new int[e.getBillCardPanel().getBillModel("id_nvo").getRowCount()]);
				}
				if(e.getBillCardPanel().getBillModel("id_cvo").getRowCount()>0){
					e.getBillCardPanel().getBillModel("id_cvo").delLine(new int[e.getBillCardPanel().getBillModel("id_cvo").getRowCount()]);
				}
				e.getBillCardPanel().getBillModel("id_cvo").addLine();
				e.getBillCardPanel().getBillModel("id_cvo").setValueAt("计划完成", 0,
						"def13");
				e.getBillCardPanel().getBodyTabbedPane().setSelectedIndex(2);
			}
		}
	}

}
