package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceCapitalmarketrepayDeleteBP {

	public void delete(AggMarketRepalayVO[] bills) {

		DeleteBPTemplate<AggMarketRepalayVO> bp = new DeleteBPTemplate<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMarketRepalayVO> processer) {
		// TODO 前规则
		IRule<AggMarketRepalayVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMarketRepalayVO> processer) {
		// TODO 后规则

	}
}
