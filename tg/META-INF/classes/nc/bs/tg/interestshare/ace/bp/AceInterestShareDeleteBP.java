package nc.bs.tg.interestshare.ace.bp;

import nc.bs.tg.interestshare.ace.rule.FN24_DelBodyAfterRule;
import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceInterestShareDeleteBP {

	public void delete(AggIntshareHead[] bills) {

		DeleteBPTemplate<AggIntshareHead> bp = new DeleteBPTemplate<AggIntshareHead>(
				InterestSharePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggIntshareHead> processer) {
		// TODO ǰ����
		IRule<AggIntshareHead> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggIntshareHead> processer) {
		// TODO �����
		FN24_DelBodyAfterRule after=new FN24_DelBodyAfterRule();
		processer.addAfterRule(after);
	}
}
