package nc.bs.tg.fischemepushstandard.ace.bp;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceFischemepushstandardDeleteBP {

	public void delete(AggFischemePushStandardHVO[] bills) {

		DeleteBPTemplate<AggFischemePushStandardHVO> bp = new DeleteBPTemplate<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFischemePushStandardHVO> processer) {
		// TODO ǰ����
		IRule<AggFischemePushStandardHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFischemePushStandardHVO> processer) {
		// TODO �����

	}
}
