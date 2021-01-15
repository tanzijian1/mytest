package nc.bs.tg.outbill.ace.bp;

import nc.bs.tg.outbill.plugin.bpplugin.OutBillPluginPoint;
import nc.vo.tgfn.outbill.AggOutbillHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceOutBillDeleteBP {

	public void delete(AggOutbillHVO[] bills) {

		DeleteBPTemplate<AggOutbillHVO> bp = new DeleteBPTemplate<AggOutbillHVO>(
				OutBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggOutbillHVO> processer) {
		// TODO ǰ����
		IRule<AggOutbillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggOutbillHVO> processer) {
		// TODO �����

	}
}
