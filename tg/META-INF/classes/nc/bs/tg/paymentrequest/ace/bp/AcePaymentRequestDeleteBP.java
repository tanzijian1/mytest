package nc.bs.tg.paymentrequest.ace.bp;

import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AcePaymentRequestDeleteBP {

	public void delete(AggPayrequest[] bills) {

		DeleteBPTemplate<AggPayrequest> bp = new DeleteBPTemplate<AggPayrequest>(
				PaymentRequestPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggPayrequest> processer) {
		// TODO ǰ����
		IRule<AggPayrequest> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggPayrequest> processer) {
		// TODO �����

	}
}
