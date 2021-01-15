package nc.bs.tg.distribution.ace.bp;

import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.distribution.AggDistribution;


/**
 * ��׼����ɾ��BP
 */
public class AceDistributionDeleteBP {

	public void delete(AggDistribution[] bills) {

		DeleteBPTemplate<AggDistribution> bp = new DeleteBPTemplate<AggDistribution>(
				DistributionPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggDistribution> processer) {
		// TODO ǰ����
		IRule<AggDistribution> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggDistribution> processer) {
		// TODO �����
	}
}
