package nc.bs.tg.financingexpense.ace.bp;

import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;


/**
 * 标准单据删除BP
 */
public class AceFinancingExpenseDeleteBP {

	public void delete(AggFinancexpenseVO[] bills) {

		DeleteBPTemplate<AggFinancexpenseVO> bp = new DeleteBPTemplate<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFinancexpenseVO> processer) {
		// TODO 前规则
		IRule<AggFinancexpenseVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFinancexpenseVO> processer) {
		// TODO 后规则

	}
}
