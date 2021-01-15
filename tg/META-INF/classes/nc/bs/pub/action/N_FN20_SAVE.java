package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.agoodsdetail.plugin.bpplugin.AGoodsDetailPluginPoint;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;
import nc.itf.tg.IAGoodsDetailMaintain;

public class N_FN20_SAVE extends AbstractPfAction<AggAGoodsDetail> {

	protected CompareAroundProcesser<AggAGoodsDetail> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAGoodsDetail> processor = new CompareAroundProcesser<AggAGoodsDetail>(
				AGoodsDetailPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggAGoodsDetail> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggAGoodsDetail[] processBP(Object userObj,
			AggAGoodsDetail[] clientFullVOs, AggAGoodsDetail[] originBills) {
		IAGoodsDetailMaintain operator = NCLocator.getInstance().lookup(
				IAGoodsDetailMaintain.class);
		AggAGoodsDetail[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
