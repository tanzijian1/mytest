package nc.bs.tg.approvalpro.ace.bp;

import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.vo.tg.approvalpro.AggApprovalProVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceApprovalproDeleteBP {

	public void delete(AggApprovalProVO[] bills) {

		DeleteBPTemplate<AggApprovalProVO> bp = new DeleteBPTemplate<AggApprovalProVO>(
				ApprovalproPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggApprovalProVO> processer) {
		// TODO 前规则
		IRule<AggApprovalProVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggApprovalProVO> processer) {
		// TODO 后规则

	}
}
