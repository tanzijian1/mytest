package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;

public class N_FN21_SAVEBASE extends AbstractPfAction<AggAbTaxTransferHVO> {

	@Override
	protected CompareAroundProcesser<AggAbTaxTransferHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAbTaxTransferHVO> processor = null;
		AggAbTaxTransferHVO[] clientFullVOs = (AggAbTaxTransferHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
					AbnormalTaxTransferPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
					AbnormalTaxTransferPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggAbTaxTransferHVO> rule = null;

		return processor;
	}

	@Override
	protected AggAbTaxTransferHVO[] processBP(Object userObj,
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills) {

		AggAbTaxTransferHVO[] bills = null;
		try {
			IAbnormalTaxTransferMaintain operator = NCLocator.getInstance()
					.lookup(IAbnormalTaxTransferMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
