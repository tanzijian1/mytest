package nc.bs.tg.changebill.ace.bp;

import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.changebill.AggChangeBillHVO;


/**
 * ��׼����ɾ��BP
 */
public class AceChangeBillDeleteBP {

	public void delete(AggChangeBillHVO[] bills) {

		DeleteBPTemplate<AggChangeBillHVO> bp = new DeleteBPTemplate<AggChangeBillHVO>(
				ChangeBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggChangeBillHVO> processer) {
		// TODO ǰ����
		IRule<AggChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggChangeBillHVO> processer) {
		// TODO �����
	}
}
