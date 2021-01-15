package nc.bs.tg.salaryfundaccure.ace.bp;

import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceSalaryfundaccureDeleteBP {

	public void delete(AggSalaryfundaccure[] bills) {

		DeleteBPTemplate<AggSalaryfundaccure> bp = new DeleteBPTemplate<AggSalaryfundaccure>(
				SalaryfundaccurePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSalaryfundaccure> processer) {
		// TODO ǰ����
		IRule<AggSalaryfundaccure> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSalaryfundaccure> processer) {
		// TODO �����

	}
}
