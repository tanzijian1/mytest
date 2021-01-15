package nc.bs.tg.invoicebill.ace.bp;

import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceInvoicebillDeleteBP {

	public void delete(AggInvoiceBillVO[] bills) {

		DeleteBPTemplate<AggInvoiceBillVO> bp = new DeleteBPTemplate<AggInvoiceBillVO>(
				InvoicebillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInvoiceBillVO> processer) {
		// TODO ǰ����
		IRule<AggInvoiceBillVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInvoiceBillVO> processer) {
		// TODO �����

	}
}
