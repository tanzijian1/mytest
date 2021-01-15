package nc.bs.tg.singleissue.ace.bp;

import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.vo.tg.singleissue.AggSingleIssueVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceSingleissueDeleteBP {

	public void delete(AggSingleIssueVO[] bills) {

		DeleteBPTemplate<AggSingleIssueVO> bp = new DeleteBPTemplate<AggSingleIssueVO>(
				SingleissuePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSingleIssueVO> processer) {
		// TODO 前规则
		IRule<AggSingleIssueVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSingleIssueVO> processer) {
		// TODO 后规则

	}
}
