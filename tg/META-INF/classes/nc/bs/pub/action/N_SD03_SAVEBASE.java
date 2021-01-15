package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.approvalpro.ace.bp.AceApprovalproChangeBP;
import nc.bs.tg.approvalpro.ace.rule.ApprovalFileRule;
import nc.bs.tg.approvalpro.ace.rule.SaveBaseBeforeRule;
import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IApprovalproMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;

public class N_SD03_SAVEBASE extends AbstractPfAction<AggApprovalProVO> {

	@Override
	protected CompareAroundProcesser<AggApprovalProVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggApprovalProVO> processor = null;
		AggApprovalProVO[] clientFullVOs = (AggApprovalProVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggApprovalProVO>(
					ApprovalproPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggApprovalProVO>(
					ApprovalproPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggApprovalProVO> rule = new ApprovalFileRule();
		processor.addBeforeRule(new SaveBaseBeforeRule());
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggApprovalProVO[] processBP(Object userObj,
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills) {

		Integer auditstate = (Integer) clientFullVOs[0].getParentVO().getApprovestatus();
		AggApprovalProVO[] bills = null;
		try {
			IApprovalproMaintain operator = NCLocator.getInstance()
					.lookup(IApprovalproMaintain.class);
			if (StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.insert(clientFullVOs, originBills);
			} else if(auditstate==null || auditstate==BillStatusEnum.FREE.value()){
				bills = operator.update(clientFullVOs, originBills);
			}else{
				for(AggApprovalProVO vo:clientFullVOs){
					vo.getParentVO().setStatus(VOStatus.UPDATED);
					vo.getParentVO().setEmendenum(0);
					vo.getParentVO().setApprover(null);
					vo.getParentVO().setApprovenote(null);
					vo.getParentVO().setApprovestatus(BillStatusEnum.FREE.toIntValue());
					vo.getParentVO().setCreator(AppContext.getInstance().getPkUser());
					vo.getParentVO().setCreationtime(AppContext.getInstance().getServerTime());
				}
				bills=new AceApprovalproChangeBP().change(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
