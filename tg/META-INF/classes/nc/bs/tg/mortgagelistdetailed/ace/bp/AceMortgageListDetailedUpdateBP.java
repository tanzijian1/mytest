package nc.bs.tg.mortgagelistdetailed.ace.bp;

import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.mortgagelistdetailed.ace.rule.InfoUniqueRule;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * 修改保存的BP
 * 
 */
public class AceMortgageListDetailedUpdateBP {

	public AggMortgageListDetailedVO[] update(
			AggMortgageListDetailedVO[] bills,
			AggMortgageListDetailedVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggMortgageListDetailedVO> bp = new UpdateBPTemplate<AggMortgageListDetailedVO>(
				FinancingExpensePluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(
			CompareAroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO 后规则
		IRule<AggMortgageListDetailedVO> rule = null;
//		processer.addAfterRule(rule);

	}

	private void addBeforeRule(
			CompareAroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO 前规则
		IRule<AggMortgageListDetailedVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		rule = new InfoUniqueRule();
		processer.addBeforeRule(rule);
	}

}
