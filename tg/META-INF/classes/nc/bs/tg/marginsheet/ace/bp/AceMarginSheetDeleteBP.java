package nc.bs.tg.marginsheet.ace.bp;

import nc.bs.tg.marginsheet.plugin.bpplugin.MarginSheetPluginPoint;
import nc.vo.tgfn.marginsheet.AggMarginHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceMarginSheetDeleteBP {

	public void delete(AggMarginHVO[] bills) {

		DeleteBPTemplate<AggMarginHVO> bp = new DeleteBPTemplate<AggMarginHVO>(
				MarginSheetPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMarginHVO> processer) {
		// TODO ǰ����
		IRule<AggMarginHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMarginHVO> processer) {
		// TODO �����

	}
}
