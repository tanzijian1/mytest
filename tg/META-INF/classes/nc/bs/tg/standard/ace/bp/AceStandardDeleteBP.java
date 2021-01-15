package nc.bs.tg.standard.ace.bp;

import nc.bs.tg.standard.ace.rule.DeleteBeforeRule;
import nc.bs.tg.standard.plugin.bpplugin.StandardPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.standard.AggStandardVO;

/**
 * ��׼����ɾ��BP
 */
public class AceStandardDeleteBP {

	public void delete(AggStandardVO[] bills) {

		DeleteBPTemplate<AggStandardVO> bp = new DeleteBPTemplate<AggStandardVO>(
				StandardPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggStandardVO> processer) {
		// TODO ǰ����
		IRule<AggStandardVO> rule = null;
		rule = new DeleteBeforeRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggStandardVO> processer) {
		// TODO �����

	}
}
