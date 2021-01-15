package nc.ui.tg.changebill.ace.model;

import java.util.Collection;

import nc.funcnode.ui.FuncletInitData;
import nc.vo.pub.link.DefaultLinkData;

public class ChangeDefaultFuncNodeInitDataListener extends nc.ui.pubapp.uif2app.model.DefaultFuncNodeInitDataListener{
   @Override
public void initData(FuncletInitData data) {
	// TODO 自动生成的方法存根
	   if(data!=null){
			if(data.getInitData()!=null){
				if(data.getInitData() instanceof nc.vo.pub.link.DefaultLinkData){
					Collection<Object> vos=((DefaultLinkData)data.getInitData()).getBillVOs();
					if(vos!=null&&vos.size()>0){
						ChangeDefaultFuncNodeInitDataListener.this.getModel().initModel(vos.toArray());
						return ;
					}
				}
			}
		}
	super.initData(data);
}
}
