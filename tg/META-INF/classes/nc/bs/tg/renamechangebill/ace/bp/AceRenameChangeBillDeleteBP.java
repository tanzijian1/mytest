package nc.bs.tg.renamechangebill.ace.bp;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;


/**
 * ��׼����ɾ��BP
 */
public class AceRenameChangeBillDeleteBP {

	public void delete(AggRenameChangeBillHVO[] bills) {

		DeleteBPTemplate<AggRenameChangeBillHVO> bp = new DeleteBPTemplate<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO ǰ����
		IRule<AggRenameChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO �����
	}
}
