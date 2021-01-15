package nc.bs.tg.projectdata.ace.bp;

import nc.bs.tg.projectdata.plugin.bpplugin.ProjectdataPluginPoint;
import nc.vo.tg.projectdata.AggProjectDataVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceProjectdataDeleteBP {

	public void delete(AggProjectDataVO[] bills) {

		DeleteBPTemplate<AggProjectDataVO> bp = new DeleteBPTemplate<AggProjectDataVO>(
				ProjectdataPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggProjectDataVO> processer) {
		// TODO 前规则
		IRule<AggProjectDataVO> rule = null;
		// rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		// processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggProjectDataVO> processer) {
		// TODO 后规则

	}
}
