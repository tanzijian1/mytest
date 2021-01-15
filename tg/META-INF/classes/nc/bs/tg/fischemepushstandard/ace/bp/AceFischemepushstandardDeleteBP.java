package nc.bs.tg.fischemepushstandard.ace.bp;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceFischemepushstandardDeleteBP {

	public void delete(AggFischemePushStandardHVO[] bills) {

		DeleteBPTemplate<AggFischemePushStandardHVO> bp = new DeleteBPTemplate<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggFischemePushStandardHVO> processer) {
		// TODO 前规则
		IRule<AggFischemePushStandardHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggFischemePushStandardHVO> processer) {
		// TODO 后规则

	}
}
