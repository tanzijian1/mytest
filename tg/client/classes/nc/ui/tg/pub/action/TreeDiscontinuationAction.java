package nc.ui.tg.pub.action;

import java.awt.event.ActionEvent;

import nc.bs.bd.util.IEnableMsgConstant;
import nc.ui.bd.pub.actions.BDTreeDisableAction;
import nc.ui.tg.pub.model.BaseTreeDataAppModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pub.SuperVO;

public class TreeDiscontinuationAction extends BDTreeDisableAction {

	@Override
	public SuperVO doDisable(SuperVO selectedVO) throws Exception {
		return (SuperVO) ((BaseTreeDataAppModel) getTreeModel())
				.enableTreeVO(selectedVO);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);
		showSuccessInfo();
	}

	@SuppressWarnings("restriction")
	protected void showSuccessInfo() {
		ShowStatusBarMsgUtil.showStatusBarMsg(IEnableMsgConstant
				.getDisableSuccessMsg(), getTreeModel().getContext());
	}
}
