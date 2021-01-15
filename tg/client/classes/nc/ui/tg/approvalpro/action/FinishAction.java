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
 * 结束按钮
 * @author wenjie
 *
 */
public class FinishAction extends NCAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5040052884064586389L;
	
	public  FinishAction(){
		setBtnName("结束");
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
		// TODO 自动生成的方法存根
		AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
		if(aggvo == null){
			ExceptionUtils.wrappBusinessException("请选择一条记录");
		}
		ApprovalProVO pvo = aggvo.getParentVO();
		if("Y".equals(pvo.getDef16())){
			ExceptionUtils.wrappBusinessException("该方案已结束，请勿重复操作！");
		}else{
			pvo.setDef16("Y");
			pvo.setDr(0);
			HYPubBO_Client.update(pvo);
			getModel().directlyUpdate(aggvo);
			ShowStatusBarMsgUtil.showStatusBarMsg("操作成功！", getModel().getContext());
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
