package nc.bs.baseapp.itfformulacfg.ace.bp;

import nc.bs.baseapp.itfformulacfg.plugin.bpplugin.ItfformulacfgPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

/**
 * @Description:����bp
 *
 * @version with NC V6.5
 */
public class AceItfformulacfgInsertBP {

	public AggFormulaCfgVO[] insert(AggFormulaCfgVO[] bills) {

		InsertBPTemplate<AggFormulaCfgVO> bp = new InsertBPTemplate<AggFormulaCfgVO>(ItfformulacfgPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * ���������
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggFormulaCfgVO> processor) {
		// TODO ���������
		IRule<AggFormulaCfgVO> rule = null;
	}

	/**
	 * ����ǰ����
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggFormulaCfgVO> processer) {
		// TODO ����ǰ����
		IRule<AggFormulaCfgVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
	}
}
