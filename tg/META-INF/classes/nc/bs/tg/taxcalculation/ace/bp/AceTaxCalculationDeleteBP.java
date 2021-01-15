package nc.bs.tg.taxcalculation.ace.bp;

import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceTaxCalculationDeleteBP {

	public void delete(AggTaxCalculationHead[] bills) {

		DeleteBPTemplate<AggTaxCalculationHead> bp = new DeleteBPTemplate<AggTaxCalculationHead>(
				TaxCalculationPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTaxCalculationHead> processer) {
		// TODO 前规则
		IRule<AggTaxCalculationHead> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTaxCalculationHead> processer) {
		// TODO 后规则

	}
}
