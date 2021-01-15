package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.agoodsdetail.plugin.bpplugin.AGoodsDetailPluginPoint;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.itf.tg.IAGoodsDetailMaintain;

public class N_FN20_APPROVE extends AbstractPfAction<AggAGoodsDetail> {

	public N_FN20_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggAGoodsDetail> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAGoodsDetail> processor = new CompareAroundProcesser<AggAGoodsDetail>(
				AGoodsDetailPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggAGoodsDetail[] processBP(Object userObj,
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills) {
		AggAGoodsDetail[] bills = null;
		IAGoodsDetailMaintain operator = NCLocator.getInstance().lookup(
				IAGoodsDetailMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
