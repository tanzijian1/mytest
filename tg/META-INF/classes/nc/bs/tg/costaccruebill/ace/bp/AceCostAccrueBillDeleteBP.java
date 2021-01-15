package nc.bs.tg.costaccruebill.ace.bp;

import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceCostAccrueBillDeleteBP {

	public void delete(AggCostAccrueBill[] bills) {

		DeleteBPTemplate<AggCostAccrueBill> bp = new DeleteBPTemplate<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggCostAccrueBill> processer) {
		// TODO 前规则
		IRule<AggCostAccrueBill> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggCostAccrueBill> processer) {
		// TODO 后规则

	}
}
