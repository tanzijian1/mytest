package nc.ui.tg.approvalpro.action;

import java.util.Map;

import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.tg.approvalpro.AggApprovalProVO;

public class ApproCommitAction extends nc.ui.pubapp.uif2app.actions.pflow.CommitScriptAction{

	@Override
	public void doBeforAction() {
		AggApprovalProVO aggvo = (AggApprovalProVO)getModel().getSelectedData();
		Map<Object, Object> eParam = getFlowContext().getEParam();
		if(aggvo.getParentVO().getEmendenum()!=null && aggvo.getParentVO().getEmendenum()==0){
			eParam.put(PfUtilBaseTools.PARAM_FORCESTART, aggvo);
		}
		// TODO 自动生成的方法存根
		super.doBeforAction();
	}
	
}
