package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceSalaryfundaccureDeleteBP {

	public void delete(AggSalaryfundaccure[] bills) {

		DeleteBPTemplate<AggSalaryfundaccure> bp = new DeleteBPTemplate<AggSalaryfundaccure>(
				SalaryfundaccurePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSalaryfundaccure> processer) {
		// TODO 前规则
		IRule<AggSalaryfundaccure> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSalaryfundaccure> processer) {
		// TODO 后规则

	}
}
