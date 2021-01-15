package nc.bs.tg.transferbill.ace.bp;

import nc.bs.tg.transferbill.plugin.bpplugin.TransferBillPluginPoint;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceTransferBillDeleteBP {

	public void delete(AggTransferBillHVO[] bills) {

		DeleteBPTemplate<AggTransferBillHVO> bp = new DeleteBPTemplate<AggTransferBillHVO>(
				TransferBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggTransferBillHVO> processer) {
		// TODO 前规则
		IRule<AggTransferBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggTransferBillHVO> processer) {
		// TODO 后规则
	}
}
