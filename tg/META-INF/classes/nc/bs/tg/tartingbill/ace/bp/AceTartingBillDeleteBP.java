package nc.bs.tg.tartingbill.ace.bp;

import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.bs.tg.tartingbill.rule.TartingDelSendGlRule;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceTartingBillDeleteBP {

	public void delete(AggTartingBillVO[] bills) {

		DeleteBPTemplate<AggTartingBillVO> bp = new DeleteBPTemplate<AggTartingBillVO>(
				TartingBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTartingBillVO> processer) {
		// TODO ǰ����
		IRule<AggTartingBillVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTartingBillVO> processer) {
		// TODO �����
	}
}
