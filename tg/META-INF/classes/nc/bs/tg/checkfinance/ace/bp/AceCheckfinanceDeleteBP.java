package nc.bs.tg.checkfinance.ace.bp;

import nc.bs.tg.checkfinance.plugin.bpplugin.CheckfinancePluginPoint;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceCheckfinanceDeleteBP {

	public void delete(AggCheckFinanceHVO[] bills) {

		DeleteBPTemplate<AggCheckFinanceHVO> bp = new DeleteBPTemplate<AggCheckFinanceHVO>(
				CheckfinancePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggCheckFinanceHVO> processer) {
		// TODO 前规则
		IRule<AggCheckFinanceHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggCheckFinanceHVO> processer) {
		// TODO 后规则

	}
}
