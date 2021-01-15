package nc.bs.tg.moonfinancingplan.ace.bp;

import nc.bs.tg.moonfinancingplan.plugin.bpplugin.MoonFinancingPlanPluginPoint;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceMoonFinancingPlanDeleteBP {

	public void delete(AggMoonFinancingPlan[] bills) {

		DeleteBPTemplate<AggMoonFinancingPlan> bp = new DeleteBPTemplate<AggMoonFinancingPlan>(
				MoonFinancingPlanPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMoonFinancingPlan> processer) {
		// TODO ǰ����
		IRule<AggMoonFinancingPlan> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMoonFinancingPlan> processer) {
		// TODO �����

	}
}
