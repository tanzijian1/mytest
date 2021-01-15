package nc.bs.tg.singleissue.ace.bp;

import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.vo.tg.singleissue.AggSingleIssueVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceSingleissueDeleteBP {

	public void delete(AggSingleIssueVO[] bills) {

		DeleteBPTemplate<AggSingleIssueVO> bp = new DeleteBPTemplate<AggSingleIssueVO>(
				SingleissuePluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSingleIssueVO> processer) {
		// TODO ǰ����
		IRule<AggSingleIssueVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSingleIssueVO> processer) {
		// TODO �����

	}
}
