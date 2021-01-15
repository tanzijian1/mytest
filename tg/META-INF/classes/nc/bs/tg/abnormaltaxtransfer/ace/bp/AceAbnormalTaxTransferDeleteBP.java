package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceAbnormalTaxTransferDeleteBP {

	public void delete(AggAbTaxTransferHVO[] bills) {

		DeleteBPTemplate<AggAbTaxTransferHVO> bp = new DeleteBPTemplate<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO 前规则
		IRule<AggAbTaxTransferHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO 后规则

	}
}
