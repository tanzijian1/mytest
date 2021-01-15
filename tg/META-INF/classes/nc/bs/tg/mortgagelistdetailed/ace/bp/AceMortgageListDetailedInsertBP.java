package nc.bs.tg.mortgagelistdetailed.ace.bp;

import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.mortgagelistdetailed.ace.rule.InfoUniqueRule;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * ��׼��������BP
 */
public class AceMortgageListDetailedInsertBP {

	public AggMortgageListDetailedVO[] insert(AggMortgageListDetailedVO[] bills) {

		InsertBPTemplate<AggMortgageListDetailedVO> bp = new InsertBPTemplate<AggMortgageListDetailedVO>(
				FinancingExpensePluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * ���������
	 * 
	 * @param processor
	 */
	private void addAfterRule(
			AroundProcesser<AggMortgageListDetailedVO> processor) {
		// TODO ���������
		IRule<AggMortgageListDetailedVO> rule = null;
	}

	/**
	 * ����ǰ����
	 * 
	 * @param processor
	 */
	private void addBeforeRule(
			AroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO ����ǰ����
		IRule<AggMortgageListDetailedVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new InfoUniqueRule();
		processer.addBeforeRule(rule);
	}
}
