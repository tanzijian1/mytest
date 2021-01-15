package nc.bs.baseapp.itfformulacfg.ace.bp;

import nc.bs.baseapp.itfformulacfg.plugin.bpplugin.ItfformulacfgPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

/**
 * @Description:更新bp
 * @version with NC V6.5
 */
public class AceItfformulacfgUpdateBP {

	public AggFormulaCfgVO[] update(AggFormulaCfgVO[] bills, AggFormulaCfgVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggFormulaCfgVO> bp = new UpdateBPTemplate<AggFormulaCfgVO>(ItfformulacfgPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggFormulaCfgVO> processer) {
		// TODO 后规则
		IRule<AggFormulaCfgVO> rule = null;

	}

	private void addBeforeRule(CompareAroundProcesser<AggFormulaCfgVO> processer) {
		// TODO 前规则
		IRule<AggFormulaCfgVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
	}

}
