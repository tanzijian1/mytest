package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.agoodsdetail.plugin.bpplugin.AGoodsDetailPluginPoint;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.itf.tg.IAGoodsDetailMaintain;

public class N_FN20_SAVEBASE extends AbstractPfAction<AggAGoodsDetail> {

	@Override
	protected CompareAroundProcesser<AggAGoodsDetail> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAGoodsDetail> processor = null;
		AggAGoodsDetail[] clientFullVOs = (AggAGoodsDetail[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggAGoodsDetail>(
					AGoodsDetailPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggAGoodsDetail>(
					AGoodsDetailPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggAGoodsDetail> rule = null;

		return processor;
	}

	@Override
	protected AggAGoodsDetail[] processBP(Object userObj,
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills) {

		AggAGoodsDetail[] bills = null;
		try {
			IAGoodsDetailMaintain operator = NCLocator.getInstance()
					.lookup(IAGoodsDetailMaintain.class);
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
