package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;

public class N_FN19_DELETE extends AbstractPfAction<AggExhousetransferbillHVO> {

	@Override
	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {
		IExHouseTransferBillMaintain operator = NCLocator.getInstance().lookup(
				IExHouseTransferBillMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
