package nc.ui.tg.singleissue.action;

import java.util.Map;

import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.tg.singleissue.AggSingleIssueVO;

public class SingleIssueCommitAction extends nc.ui.pubapp.uif2app.actions.pflow.CommitScriptAction{
	
	@Override
	public void doBeforAction() {
		AggSingleIssueVO aggvo = (AggSingleIssueVO)getModel().getSelectedData();
		Map<Object, Object> eParam = getFlowContext().getEParam();
		if(aggvo.getParentVO().getEmendenum() != null && aggvo.getParentVO().getEmendenum()==0){
			eParam.put(PfUtilBaseTools.PARAM_FORCESTART, aggvo);
		}
		super.doBeforAction();
	}
	
}
