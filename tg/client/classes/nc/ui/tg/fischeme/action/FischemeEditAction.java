package nc.ui.tg.fischeme.action;

import nc.vo.tg.fischeme.AggFIScemeHVO;

public class FischemeEditAction extends nc.ui.pubapp.uif2app.actions.EditAction{
@Override
protected boolean isActionEnable() {
	// TODO �Զ����ɵķ������
	boolean flag=true;//�Ƿ��ѱ����
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
