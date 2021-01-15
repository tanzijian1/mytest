package nc.bs.tg.checkfinance.ace.bp;

import nc.bs.tg.checkfinance.plugin.bpplugin.CheckfinancePluginPoint;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceCheckfinanceDeleteBP {

	public void delete(AggCheckFinanceHVO[] bills) {

		DeleteBPTemplate<AggCheckFinanceHVO> bp = new DeleteBPTemplate<AggCheckFinanceHVO>(
				CheckfinancePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggCheckFinanceHVO> processer) {
		// TODO ǰ����
		IRule<AggCheckFinanceHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggCheckFinanceHVO> processer) {
		// TODO �����

	}
}
