package nc.bs.tg.addticket.ace.bp;

import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.vo.tg.addticket.AggAddTicket;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceAddticketDeleteBP {

	public void delete(AggAddTicket[] bills) {

		DeleteBPTemplate<AggAddTicket> bp = new DeleteBPTemplate<AggAddTicket>(
				AddticketPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggAddTicket> processer) {
		// TODO ǰ����
		IRule<AggAddTicket> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggAddTicket> processer) {
		// TODO �����

	}
}
