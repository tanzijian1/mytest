package nc.bs.tg.targetactivation.ace.bp;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.targetactivation.AggTargetactivation;


/**
 * ��׼����ɾ��BP
 */
public class AceTargetActivationDeleteBP {

	public void delete(AggTargetactivation[] bills) {

		DeleteBPTemplate<AggTargetactivation> bp = new DeleteBPTemplate<AggTargetactivation>(
				TargetActivationPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTargetactivation> processer) {
		// TODO ǰ����
		IRule<AggTargetactivation> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTargetactivation> processer) {
		// TODO �����

	}
}
