package nc.bs.tg.financingtotal.ace.bp;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceFinancingTotalDeleteBP {

	public void delete(AggFinancingTotal[] bills) {

		DeleteBPTemplate<AggFinancingTotal> bp = new DeleteBPTemplate<AggFinancingTotal>(
				FinancingTotalPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFinancingTotal> processer) {
		// TODO 前规则
		IRule<AggFinancingTotal> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFinancingTotal> processer) {
		// TODO 后规则

	}
}
