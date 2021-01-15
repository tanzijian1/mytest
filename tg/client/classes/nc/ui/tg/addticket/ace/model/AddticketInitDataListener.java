package nc.ui.tg.addticket.ace.model;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.ui.pubapp.uif2app.view.util.BillPanelUtils;
import nc.ui.uif2.UIState;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.uif2.LoginContext;

public class AddticketInitDataListener extends DefaultFuncNodeInitDataListener{
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm billform;
	private nc.ui.pubapp.uif2app.view.ShowUpableBillListView  billListView;
	
@Override
public void initData(FuncletInitData data) {
	// TODO 自动生成的方法存根
	super.initData(data);
	if(data!=null){
		if(data.getInitData() instanceof  DefaultLinkData){
		if("qingkuan".equals(((DefaultLinkData)data.getInitData()).getBillType())){
			LoginContext context = getBillform().getModel().getContext();
			
			// 进行参照过滤
			BillPanelUtils.setOrgForAllRef(getBillform().getBillCardPanel(),
					context);
			AggAddTicket aggvo=(AggAddTicket)((DefaultLinkData)data.getInitData()).getUserObject();
//			billform.showMeUp();
//			this.getModel().initModel(aggvos);
			
//			getModel().setUiState(UIState.EDIT);
//			getBillListView().getBillListPanel(
			
			
			getBillform().showMeUp();
			getModel().setOtherUiState(UIState.NOT_EDIT);
			getModel().setUiState(UIState.ADD);
			getBillform().addNew();
			getBillform().showMeUp();			
			getBillform().setValue(aggvo);
			getBillform().setEditable(true);
			getBillform().setEnabled(true);	
			String def2 = aggvo.getParentVO().getDef2();
			String def36 = aggvo.getParentVO().getDef36();
			if(def2 != null){//合同编号不为空，单期发行不可编辑
				getBillform().getBillCardPanel().getHeadItem("def36").setEnabled(false);
			}else if(def36 != null){
				getBillform().getBillCardPanel().getHeadItem("def2").setEnabled(false);
			}
			
			
//			getBillform().getBillCardPanel().getBillModel().delLine(new int[0]);
//			getBillform().getBillCardPanel().getBillModel().getRowCount();
//			getBillform().getBillCardPanel().getBillModel().delLine(new int[0]);
			
			
			return ;
		}
	 }
	}
}
public nc.ui.pubapp.uif2app.view.ShowUpableBillForm getBillform() {
	return billform;
}
public void setBillform(nc.ui.pubapp.uif2app.view.ShowUpableBillForm billform) {
	this.billform = billform;
}
public nc.ui.pubapp.uif2app.view.ShowUpableBillListView getBillListView() {
	return billListView;
}
public void setBillListView(
		nc.ui.pubapp.uif2app.view.ShowUpableBillListView billListView) {
	this.billListView = billListView;
}

}
