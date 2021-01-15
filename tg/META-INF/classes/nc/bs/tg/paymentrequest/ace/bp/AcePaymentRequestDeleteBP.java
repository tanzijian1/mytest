package nc.bs.tg.paymentrequest.ace.bp;

import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AcePaymentRequestDeleteBP {

	public void delete(AggPayrequest[] bills) {

		DeleteBPTemplate<AggPayrequest> bp = new DeleteBPTemplate<AggPayrequest>(
				PaymentRequestPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggPayrequest> processer) {
		// TODO 前规则
		IRule<AggPayrequest> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggPayrequest> processer) {
		// TODO 后规则

	}
}
