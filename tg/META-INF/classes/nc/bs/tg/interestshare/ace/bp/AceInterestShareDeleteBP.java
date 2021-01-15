package nc.bs.tg.interestshare.ace.bp;

import nc.bs.tg.interestshare.ace.rule.FN24_DelBodyAfterRule;
import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceInterestShareDeleteBP {

	public void delete(AggIntshareHead[] bills) {

		DeleteBPTemplate<AggIntshareHead> bp = new DeleteBPTemplate<AggIntshareHead>(
				InterestSharePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggIntshareHead> processer) {
		// TODO 前规则
		IRule<AggIntshareHead> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggIntshareHead> processer) {
		// TODO 后规则
		FN24_DelBodyAfterRule after=new FN24_DelBodyAfterRule();
		processer.addAfterRule(after);
	}
}
