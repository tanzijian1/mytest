package nc.bs.tg.moonfinancingplan.ace.bp;

import nc.bs.tg.moonfinancingplan.plugin.bpplugin.MoonFinancingPlanPluginPoint;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceMoonFinancingPlanDeleteBP {

	public void delete(AggMoonFinancingPlan[] bills) {

		DeleteBPTemplate<AggMoonFinancingPlan> bp = new DeleteBPTemplate<AggMoonFinancingPlan>(
				MoonFinancingPlanPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMoonFinancingPlan> processer) {
		// TODO 前规则
		IRule<AggMoonFinancingPlan> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMoonFinancingPlan> processer) {
		// TODO 后规则

	}
}
