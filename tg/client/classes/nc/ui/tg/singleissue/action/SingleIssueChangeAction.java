package nc.ui.tg.singleissue.action;

import java.awt.Container;
import java.awt.event.ActionEvent;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue.SingleIssueVO;

public class SingleIssueChangeAction extends nc.ui.pubapp.uif2app.actions.EditAction{
	public SingleIssueChangeAction(){
		setBtnName("���");
		setCode("singleChangeAction");
	}
	private nc.ui.tg.singleissue.ace.view.SingleissueBillForm billForm;
	
	@Override
	protected boolean isActionEnable() {
		AggSingleIssueVO aggvo = (AggSingleIssueVO)getModel().getSelectedData();
		if(aggvo != null){
			SingleIssueVO pVO = aggvo.getParentVO();
			Integer status = pVO.getApprovestatus();
			if(status == 1){
				return true;
			}
		}
		return false;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		int i=MessageDialog.showOkCancelDlg((Container)e.getSource(), "���ȷ��", "ȷ���Ƿ�������");
		if(i==1){
			AggSingleIssueVO aggvo = (AggSingleIssueVO)getModel().getSelectedData();
			if(aggvo==null){
				throw new BusinessException("��ѡ������");
			}
			getModel().setUiState(UIState.EDIT);
			this.getBillForm().getBillCardPanel().getHeadItem("def1").setEnabled(false);//��������
			this.getBillForm().getBillCardPanel().getHeadItem("def42").setEnabled(false);//ҵ������
		}
		
	}

	public nc.ui.tg.singleissue.ace.view.SingleissueBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(nc.ui.tg.singleissue.ace.view.SingleissueBillForm billForm) {
		this.billForm = billForm;
	}
}
