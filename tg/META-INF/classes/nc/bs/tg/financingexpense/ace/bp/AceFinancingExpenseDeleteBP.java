package nc.bs.tg.financingexpense.ace.bp;

import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;


/**
 * ��׼����ɾ��BP
 */
public class AceFinancingExpenseDeleteBP {

	public void delete(AggFinancexpenseVO[] bills) {

		DeleteBPTemplate<AggFinancexpenseVO> bp = new DeleteBPTemplate<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFinancexpenseVO> processer) {
		// TODO ǰ����
		IRule<AggFinancexpenseVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFinancexpenseVO> processer) {
		// TODO �����

	}
}
