package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;

public class N_FN19_SAVEBASE extends AbstractPfAction<AggExhousetransferbillHVO> {

	@Override
	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = null;
		AggExhousetransferbillHVO[] clientFullVOs = (AggExhousetransferbillHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
					ExHouseTransferBillPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
					ExHouseTransferBillPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggExhousetransferbillHVO> rule = null;

		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {

		AggExhousetransferbillHVO[] bills = null;
		try {
			IExHouseTransferBillMaintain operator = NCLocator.getInstance()
					.lookup(IExHouseTransferBillMaintain.class);
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
