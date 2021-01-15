package nc.ui.tg.capitalmarketrepay.ace.view;

import java.util.Arrays;
import java.util.Comparator;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.uif2.UIState;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.link.DefaultLinkData;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;

public class MarketInitDataListener extends nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener{
	private nc.ui.pubapp.uif2app.view.ShowUpableBillForm billform;
	private nc.ui.pubapp.uif2app.view.ShowUpableBillListView  billListView;
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
	@Override
	public void initData(FuncletInitData data) {
		// TODO 自动生成的方法存根
		super.initData(data);
		if(data!=null){
			if(data.getInitData() instanceof  DefaultLinkData){
				if("huankuan".equals(((DefaultLinkData)data.getInitData()).getBillType())){
					AggMarketRepalayVO aggvo=(AggMarketRepalayVO)((DefaultLinkData)data.getInitData()).getUserObject();
					MarketRepaleyBVO[] vos = (MarketRepaleyBVO[]) aggvo.getChildren(MarketRepaleyBVO.class);
					Arrays.sort(vos, new Comparator<MarketRepaleyBVO>() {
						@Override
						public int compare(MarketRepaleyBVO o1,
								MarketRepaleyBVO o2) {
							UFDouble v1 = o1.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o1.getDef3());
							UFDouble v2 = o2.getDef3()==null?UFDouble.ZERO_DBL:new UFDouble(o2.getDef3());
							if(o1.getDef2().compareTo(o2.getDef2())==0){
								return v1.compareTo(v2);
							}
							return o1.getDef2().compareTo(o2.getDef2());
						}
					});
					getBillform().showMeUp();
					getModel().setOtherUiState(UIState.NOT_EDIT);
					getModel().setUiState(UIState.ADD);
					getBillform().addNew();
					getBillform().showMeUp();			
					getBillform().setValue(aggvo);
					getBillform().setEditable(true);
					getBillform().setEnabled(true);	
				}
			}
		}
	}
	
}
