package nc.bs.tg.marginworkorder.ace.bp;

import nc.bs.tg.marginworkorder.plugin.bpplugin.MarginWorkorderPluginPoint;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceMarginWorkorderDeleteBP {

	public void delete(AggMarginHVO[] bills) {

		DeleteBPTemplate<AggMarginHVO> bp = new DeleteBPTemplate<AggMarginHVO>(
				MarginWorkorderPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMarginHVO> processer) {
		// TODO 前规则
		IRule<AggMarginHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMarginHVO> processer) {
		// TODO 后规则

	}
}
