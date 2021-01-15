package nc.bs.tg.changebill.ace.bp;

import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.changebill.AggChangeBillHVO;


/**
 * 标准单据删除BP
 */
public class AceChangeBillDeleteBP {

	public void delete(AggChangeBillHVO[] bills) {

		DeleteBPTemplate<AggChangeBillHVO> bp = new DeleteBPTemplate<AggChangeBillHVO>(
				ChangeBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggChangeBillHVO> processer) {
		// TODO 前规则
		IRule<AggChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggChangeBillHVO> processer) {
		// TODO 后规则
	}
}
