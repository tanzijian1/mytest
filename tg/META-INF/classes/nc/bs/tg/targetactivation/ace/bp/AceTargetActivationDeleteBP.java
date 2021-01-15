package nc.bs.tg.targetactivation.ace.bp;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.targetactivation.AggTargetactivation;


/**
 * 标准单据删除BP
 */
public class AceTargetActivationDeleteBP {

	public void delete(AggTargetactivation[] bills) {

		DeleteBPTemplate<AggTargetactivation> bp = new DeleteBPTemplate<AggTargetactivation>(
				TargetActivationPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTargetactivation> processer) {
		// TODO 前规则
		IRule<AggTargetactivation> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTargetactivation> processer) {
		// TODO 后规则

	}
}
