package nc.bs.tg.projectdata.ace.bp;

import nc.bs.tg.projectdata.plugin.bpplugin.ProjectdataPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.projectdata.AggProjectDataVO;

/**
 * 修改保存的BP
 * 
 */
public class AceProjectdataUpdateBP {

	public AggProjectDataVO[] update(AggProjectDataVO[] bills,
			AggProjectDataVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggProjectDataVO> bp = new UpdateBPTemplate<AggProjectDataVO>(
				ProjectdataPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggProjectDataVO> processer) {
		// TODO 后规则
		IRule<AggProjectDataVO> rule = null;

	}

	private void addBeforeRule(CompareAroundProcesser<AggProjectDataVO> processer) {
		// TODO 前规则
		IRule<AggProjectDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
	}

}
