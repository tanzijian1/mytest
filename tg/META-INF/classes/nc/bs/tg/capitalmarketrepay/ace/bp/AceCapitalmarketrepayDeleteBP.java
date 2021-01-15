package nc.bs.tg.capitalmarketrepay.ace.bp;

import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceCapitalmarketrepayDeleteBP {

	public void delete(AggMarketRepalayVO[] bills) {

		DeleteBPTemplate<AggMarketRepalayVO> bp = new DeleteBPTemplate<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMarketRepalayVO> processer) {
		// TODO ǰ����
		IRule<AggMarketRepalayVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMarketRepalayVO> processer) {
		// TODO �����

	}
}
