package nc.bs.tg.renamechangebill.ace.bp;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;


/**
 * 标准单据删除BP
 */
public class AceRenameChangeBillDeleteBP {

	public void delete(AggRenameChangeBillHVO[] bills) {

		DeleteBPTemplate<AggRenameChangeBillHVO> bp = new DeleteBPTemplate<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO 前规则
		IRule<AggRenameChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO 后规则
	}
}
