package nc.bs.tg.standard.ace.bp;

import nc.bs.tg.standard.ace.rule.DeleteBeforeRule;
import nc.bs.tg.standard.plugin.bpplugin.StandardPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.standard.AggStandardVO;

/**
 * 标准单据删除BP
 */
public class AceStandardDeleteBP {

	public void delete(AggStandardVO[] bills) {

		DeleteBPTemplate<AggStandardVO> bp = new DeleteBPTemplate<AggStandardVO>(
				StandardPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggStandardVO> processer) {
		// TODO 前规则
		IRule<AggStandardVO> rule = null;
		rule = new DeleteBeforeRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggStandardVO> processer) {
		// TODO 后规则

	}
}
