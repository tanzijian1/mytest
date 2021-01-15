package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.agoodsdetail.plugin.bpplugin.AGoodsDetailPluginPoint;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.itf.tg.IAGoodsDetailMaintain;

public class N_FN20_UNSAVEBILL extends AbstractPfAction<AggAGoodsDetail> {

	@Override
	protected CompareAroundProcesser<AggAGoodsDetail> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAGoodsDetail> processor = new CompareAroundProcesser<AggAGoodsDetail>(
				AGoodsDetailPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggAGoodsDetail[] processBP(Object userObj,
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills) {
		IAGoodsDetailMaintain operator = NCLocator.getInstance().lookup(
				IAGoodsDetailMaintain.class);
		AggAGoodsDetail[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
