package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.singleissue.ace.bp.AceSingleissueChangeBP;
import nc.bs.tg.singleissue.ace.rule.SingleIssueFileRule;
import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ISingleissueMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;

public class N_SD06_SAVEBASE extends AbstractPfAction<AggSingleIssueVO> {

	@Override
	protected CompareAroundProcesser<AggSingleIssueVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSingleIssueVO> processor = null;
		AggSingleIssueVO[] clientFullVOs = (AggSingleIssueVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSingleIssueVO>(
					SingleissuePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSingleIssueVO>(
					SingleissuePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSingleIssueVO> rule = new SingleIssueFileRule();
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggSingleIssueVO[] processBP(Object userObj,
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills) {

		Integer auditstate = (Integer) clientFullVOs[0].getParentVO().getApprovestatus();
		AggSingleIssueVO[] bills = null;
		try {
			ISingleissueMaintain operator = NCLocator.getInstance()
					.lookup(ISingleissueMaintain.class);
			if (StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.insert(clientFullVOs, originBills);
			} else if(auditstate==null || auditstate==BillStatusEnum.FREE.value()){
				bills = operator.update(clientFullVOs, originBills);
			}else{
				for (AggSingleIssueVO vo : clientFullVOs) {
					vo.getParentVO().setStatus(VOStatus.UPDATED);
					vo.getParentVO().setEmendenum(0);
					vo.getParentVO().setApprover(null);
					vo.getParentVO().setApprovenote(null);
					vo.getParentVO().setApprovestatus(BillStatusEnum.FREE.toIntValue());
					vo.getParentVO().setCreator(AppContext.getInstance().getPkUser());
					vo.getParentVO().setCreationtime(AppContext.getInstance().getServerTime());
				}
				bills = new AceSingleissueChangeBP().change(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
