package nc.bs.tg.masterdata.ace.bp;

import nc.bs.tg.masterdata.plugin.bpplugin.MasterdataPluginPoint;
import nc.vo.tg.masterdata.AggMasterDataVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceMasterdataDeleteBP {

	public void delete(AggMasterDataVO[] bills) {

		DeleteBPTemplate<AggMasterDataVO> bp = new DeleteBPTemplate<AggMasterDataVO>(
				MasterdataPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMasterDataVO> processer) {
		// TODO 前规则
		IRule<AggMasterDataVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMasterDataVO> processer) {
		// TODO 后规则

	}
}
