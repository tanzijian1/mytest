package nc.ui.tg.fischeme.action;

import nc.vo.tg.fischeme.AggFIScemeHVO;

public class FischemeEditAction extends nc.ui.pubapp.uif2app.actions.EditAction{
@Override
protected boolean isActionEnable() {
	// TODO 自动生成的方法存根
	boolean flag=true;//是否已变更过
	if(getModel()!=null){
		if(getModel().getSelectedData()!=null){
		AggFIScemeHVO billvo=(AggFIScemeHVO)getModel().getSelectedData();
		if("Y".equals(billvo.getParentVO().getVdef10())){
			flag=false;
		}
		}
	}
	return super.isActionEnable()&&flag;
}
}
