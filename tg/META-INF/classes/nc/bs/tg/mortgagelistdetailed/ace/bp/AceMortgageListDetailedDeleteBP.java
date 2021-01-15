package nc.bs.tg.mortgagelistdetailed.ace.bp;

import nc.bs.tg.mortgagelistdetailed.plugin.bpplugin.MortgageListDetailedPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * 标准单据删除BP
 */
public class AceMortgageListDetailedDeleteBP {

	public void delete(AggMortgageListDetailedVO[] bills) {

		DeleteBPTemplate<AggMortgageListDetailedVO> bp = new DeleteBPTemplate<AggMortgageListDetailedVO>(
				MortgageListDetailedPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO 前规则
		IRule<AggMortgageListDetailedVO> rule = null;
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(
			AroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO 后规则

	}
}
