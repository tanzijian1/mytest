package nc.ui.tg.approvalpro.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro.ApprovalProVO;
/**
 * ������ť
 * @author wenjie
 *
 */
public class FinishAction extends NCAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5040052884064586389L;
	
	public  FinishAction(){
		setBtnName("����");
		setCode("finishAction");
	}
	private BillManageModel model;
	private ShowUpableBillForm editor;
	
	@Override
	protected boolean isActionEnable() {
		AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
		if(aggvo == null){
			return false;
		}
		if(aggvo.getParentVO().getApprovestatus()==1 && !"Y".equals(aggvo.getParentVO().getDef16())){
			return true;
		}
		return false;
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// TODO �Զ����ɵķ������
		AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
		if(aggvo == null){
			ExceptionUtils.wrappBusinessException("��ѡ��һ����¼");
		}
		ApprovalProVO pvo = aggvo.getParentVO();
		if("Y".equals(pvo.getDef16())){
			ExceptionUtils.wrappBusinessException("�÷����ѽ����������ظ�������");
		}else{
			pvo.setDef16("Y");
			pvo.setDr(0);
			HYPubBO_Client.update(pvo);
			getModel().directlyUpdate(aggvo);
			ShowStatusBarMsgUtil.showStatusBarMsg("�����ɹ���", getModel().getContext());
		}
	}

	public BillManageModel getModel() {
		return model;
	}

	public void setModel(BillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}

}
