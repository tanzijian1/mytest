package nc.bs.tg.tartingbill.ace.bp;

import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.bs.tg.tartingbill.rule.TartingDelSendGlRule;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceTartingBillDeleteBP {

	public void delete(AggTartingBillVO[] bills) {

		DeleteBPTemplate<AggTartingBillVO> bp = new DeleteBPTemplate<AggTartingBillVO>(
				TartingBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTartingBillVO> processer) {
		// TODO 前规则
		IRule<AggTartingBillVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTartingBillVO> processer) {
		// TODO 后规则
	}
}
