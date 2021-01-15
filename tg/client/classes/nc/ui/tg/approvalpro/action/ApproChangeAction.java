package nc.ui.tg.approvalpro.action;

import java.awt.Container;
import java.awt.event.ActionEvent;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ApprovalProVO;

public class ApproChangeAction extends nc.ui.pubapp.uif2app.actions.EditAction{
	public ApproChangeAction(){
		setBtnName("变更");
		setCode("approChangeAction");
	}
	private nc.ui.tg.approvalpro.ace.view.BillForm editor;
	
	@Override
	protected boolean isActionEnable() {
		AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
		if(aggvo != null){
			ApprovalProVO pVO = aggvo.getParentVO();
			Integer status = pVO.getApprovestatus();
			if(status == 1){
				return true;
			}
		}
		return false;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		int i=MessageDialog.showOkCancelDlg((Container)e.getSource(), "变更确认", "确认是否继续变更");
		if(i==1){
			AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
			if(aggvo==null){
				throw new BusinessException("请选中数据");
			}
			getModel().setUiState(UIState.EDIT);
			this.getEditor().getBillCardPanel().getHeadItem("def1").setEnabled(false);
		}
		
	}

	public nc.ui.tg.approvalpro.ace.view.BillForm getEditor() {
		return editor;
	}

	public void setEditor(nc.ui.tg.approvalpro.ace.view.BillForm editor) {
		this.editor = editor;
	}
	
	
}
