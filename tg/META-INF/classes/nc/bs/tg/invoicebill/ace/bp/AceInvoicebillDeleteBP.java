package nc.bs.tg.invoicebill.ace.bp;

import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceInvoicebillDeleteBP {

	public void delete(AggInvoiceBillVO[] bills) {

		DeleteBPTemplate<AggInvoiceBillVO> bp = new DeleteBPTemplate<AggInvoiceBillVO>(
				InvoicebillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInvoiceBillVO> processer) {
		// TODO 前规则
		IRule<AggInvoiceBillVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInvoiceBillVO> processer) {
		// TODO 后规则

	}
}
