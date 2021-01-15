package nc.bs.tg.exhousetransferbill.ace.bp;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;


/**
 * 标准单据删除BP
 */
public class AceExHouseTransferBillDeleteBP {

	public void delete(AggExhousetransferbillHVO[] bills) {

		DeleteBPTemplate<AggExhousetransferbillHVO> bp = new DeleteBPTemplate<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggExhousetransferbillHVO> processer) {
		// TODO 前规则
		IRule<AggExhousetransferbillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggExhousetransferbillHVO> processer) {
		// TODO 后规则
	}
}
