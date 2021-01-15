package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;

public class N_FN21_SAVE extends AbstractPfAction<AggAbTaxTransferHVO> {

	protected CompareAroundProcesser<AggAbTaxTransferHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAbTaxTransferHVO> processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggAbTaxTransferHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggAbTaxTransferHVO[] processBP(Object userObj,
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills) {
		IAbnormalTaxTransferMaintain operator = NCLocator.getInstance().lookup(
				IAbnormalTaxTransferMaintain.class);
		AggAbTaxTransferHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
