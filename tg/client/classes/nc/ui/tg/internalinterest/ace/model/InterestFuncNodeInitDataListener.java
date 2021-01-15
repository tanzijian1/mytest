package nc.ui.tg.internalinterest.ace.model;

import nc.funcnode.ui.FuncletInitData;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener;
import nc.ui.pubapp.uif2app.model.SimpleQueryByPk;
import nc.ui.uif2.UIState;
import nc.vo.pub.link.DefaultLinkData;

public class InterestFuncNodeInitDataListener extends nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener{
   @Override
public void initData(FuncletInitData data) {
	// TODO 自动生成的方法存根
	   if (UIState.EDIT.equals(this.getModel().getUiState())
				|| UIState.ADD.equals(this.getModel().getUiState())) {
			return;
		}

		if (null == data) {
			this.getModel().initModel(null);
			return;
		}
		if(ILinkType.LINK_TYPE_QUERY==data.getInitType()){
		DefaultLinkData idata=(DefaultLinkData)data.getInitData();
		IQueryServiceWithFuncCodeExt serv=	new SimpleQueryByPk(this);
		Object[] objs=serv.queryByPksWithFunCode(idata.getBillIDs(), InterestFuncNodeInitDataListener.this.getContext()
				.getNodeCode());
		InterestFuncNodeInitDataListener.this.getModel().initModel(objs);
		}else{
		    super.initData(data);
		}
}
}
