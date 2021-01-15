package nc.bs.tg.rzreportbi.ace.bp;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;

/**
 * ��׼��������BP
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
	 * ���������
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggRZreportBIVO> processor) {
		// TODO ���������
		IRule<AggRZreportBIVO> rule = null;
	}

	/**
	 * ����ǰ����
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggRZreportBIVO> processer) {
		// TODO ����ǰ����
		IRule<AggRZreportBIVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
	}
}
