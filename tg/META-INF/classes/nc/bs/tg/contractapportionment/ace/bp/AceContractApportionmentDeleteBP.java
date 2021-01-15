package nc.bs.tg.contractapportionment.ace.bp;

import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceContractApportionmentDeleteBP {

	public void delete(AggContractAptmentVO[] bills) {

		DeleteBPTemplate<AggContractAptmentVO> bp = new DeleteBPTemplate<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggContractAptmentVO> processer) {
		// TODO 前规则
		IRule<AggContractAptmentVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggContractAptmentVO> processer) {
		// TODO 后规则

	}
}
