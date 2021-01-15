package nc.bs.tg.rzreportbi.ace.bp;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;

/**
 * 修改保存的BP
 * 
 */
public class AceRZreportBIUpdateBP {

	public AggRZreportBIVO[] update(AggRZreportBIVO[] bills,
			AggRZreportBIVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggRZreportBIVO> bp = new UpdateBPTemplate<AggRZreportBIVO>(
				RZreportBIPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggRZreportBIVO> processer) {
		// TODO 后规则
		IRule<AggRZreportBIVO> rule = null;

	}

	private void addBeforeRule(CompareAroundProcesser<AggRZreportBIVO> processer) {
		// TODO 前规则
		IRule<AggRZreportBIVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
	}

}
