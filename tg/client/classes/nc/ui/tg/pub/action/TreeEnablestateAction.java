package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.bs.bd.util.IEnableMsgConstant;
import nc.ui.bd.pub.actions.BDEnableAction;
import nc.ui.tg.pub.model.BaseTreeDataAppModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;

public class TreeEnablestateAction extends BDEnableAction {

	@Override
	public Object doEnable(Object obj) throws Exception {
		return ((BaseTreeDataAppModel) getModel()).enableTreeVO(obj);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);
		showSuccessInfo();
	}

	protected void showSuccessInfo() {
		ShowStatusBarMsgUtil.showStatusBarMsg(IEnableMsgConstant
				.getEnableSuccessMsg(), getModel().getContext());
	}
}
