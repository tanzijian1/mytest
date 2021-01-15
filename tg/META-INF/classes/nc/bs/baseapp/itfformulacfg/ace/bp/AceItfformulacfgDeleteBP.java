package nc.bs.baseapp.itfformulacfg.ace.bp;

import nc.bs.baseapp.itfformulacfg.plugin.bpplugin.ItfformulacfgPluginPoint;
import nc.vo.baseapp.itfformulacfg.AggFormulaCfgVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;

/**
 * @Description:删除bp
 * @version with NC V6.5
 */
public class AceItfformulacfgDeleteBP {

	public void delete(AggFormulaCfgVO[] bills) {

		DeleteBPTemplate<AggFormulaCfgVO> bp = new DeleteBPTemplate<AggFormulaCfgVO>(ItfformulacfgPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFormulaCfgVO> processer) {
		// TODO 前规则
		IRule<AggFormulaCfgVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFormulaCfgVO> processer) {
		// TODO 后规则

	}
}
