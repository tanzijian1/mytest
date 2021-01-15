package nc.bs.tg.internalinterest.ace.bp;

import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceInternalInterestDeleteBP {

	public void delete(AggInternalinterest[] bills) {

		DeleteBPTemplate<AggInternalinterest> bp = new DeleteBPTemplate<AggInternalinterest>(
				InternalInterestPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggInternalinterest> processer) {
		// TODO ǰ����
		IRule<AggInternalinterest> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggInternalinterest> processer) {
		// TODO �����

	}
}
