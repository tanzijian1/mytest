package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;

public class N_FN21_DELETE extends AbstractPfAction<AggAbTaxTransferHVO> {

	@Override
	protected CompareAroundProcesser<AggAbTaxTransferHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAbTaxTransferHVO> processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggAbTaxTransferHVO[] processBP(Object userObj,
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills) {
		IAbnormalTaxTransferMaintain operator = NCLocator.getInstance().lookup(
				IAbnormalTaxTransferMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
