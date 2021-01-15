package nc.bs.tg.internalinterest.ace.bp;

import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceInternalInterestDeleteBP {

	public void delete(AggInternalinterest[] bills) {

		DeleteBPTemplate<AggInternalinterest> bp = new DeleteBPTemplate<AggInternalinterest>(
				InternalInterestPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInternalinterest> processer) {
		// TODO 前规则
		IRule<AggInternalinterest> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInternalinterest> processer) {
		// TODO 后规则

	}
}
