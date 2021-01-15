package nc.bs.tg.financingtotal.ace.bp;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceFinancingTotalDeleteBP {

	public void delete(AggFinancingTotal[] bills) {

		DeleteBPTemplate<AggFinancingTotal> bp = new DeleteBPTemplate<AggFinancingTotal>(
				FinancingTotalPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFinancingTotal> processer) {
		// TODO ǰ����
		IRule<AggFinancingTotal> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFinancingTotal> processer) {
		// TODO �����

	}
}
