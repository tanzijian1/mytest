package nc.bs.tg.mortgagelistdetailed.ace.bp;

import nc.bs.tg.mortgagelistdetailed.plugin.bpplugin.MortgageListDetailedPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.mortgagelist.AggMortgageListDetailedVO;

/**
 * ��׼����ɾ��BP
 */
public class AceMortgageListDetailedDeleteBP {

	public void delete(AggMortgageListDetailedVO[] bills) {

		DeleteBPTemplate<AggMortgageListDetailedVO> bp = new DeleteBPTemplate<AggMortgageListDetailedVO>(
				MortgageListDetailedPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO ǰ����
		IRule<AggMortgageListDetailedVO> rule = null;
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(
			AroundProcesser<AggMortgageListDetailedVO> processer) {
		// TODO �����

	}
}
