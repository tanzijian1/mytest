package nc.bs.tg.agoodsdetail.ace.bp;

import nc.bs.tg.agoodsdetail.plugin.bpplugin.AGoodsDetailPluginPoint;
import nc.vo.tgfn.agoodsdetail.AggAGoodsDetail;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceAGoodsDetailDeleteBP {

	public void delete(AggAGoodsDetail[] bills) {

		DeleteBPTemplate<AggAGoodsDetail> bp = new DeleteBPTemplate<AggAGoodsDetail>(
				AGoodsDetailPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggAGoodsDetail> processer) {
		// TODO 前规则
		IRule<AggAGoodsDetail> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggAGoodsDetail> processer) {
		// TODO 后规则

	}
}
