package nc.bs.tg.mortgagelistdetailed.ace.bp;

import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.mortgagelistdetailed.ace.rule.InfoUniqueRule;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * �޸ı����BP
 * 
 */
public class AceMortgageListDetailedUpdateBP {

	public AggMortgageListDetailedVO[] update(
			AggMortgageListDetailedVO[] bills,
			AggMortgageListDetailedVO[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggMortgageListDetailedVO> bp = new UpdateBPTemplate<AggMortgageListDetailedVO>(
				FinancingExpensePluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(
			CompareAroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO �����
		IRule<AggMortgageListDetailedVO> rule = null;
//		processer.addAfterRule(rule);

	}

	private void addBeforeRule(
			CompareAroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO ǰ����
		IRule<AggMortgageListDetailedVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		rule = new InfoUniqueRule();
		processer.addBeforeRule(rule);
	}

}
