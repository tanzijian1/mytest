package nc.bs.tg.rzreportbi.ace.bp;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;

/**
 * 标准单据新增BP
 */
public class AceRZreportBIInsertBP {

	public AggRZreportBIVO[] insert(AggRZreportBIVO[] bills) {

		InsertBPTemplate<AggRZreportBIVO> bp = new InsertBPTemplate<AggRZreportBIVO>(
				RZreportBIPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggRZreportBIVO> processor) {
		// TODO 新增后规则
		IRule<AggRZreportBIVO> rule = null;
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggRZreportBIVO> processer) {
		// TODO 新增前规则
		IRule<AggRZreportBIVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
	}
}
