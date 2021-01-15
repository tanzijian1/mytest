package nc.bs.tg.contractapportionment.ace.bp;

import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceContractApportionmentDeleteBP {

	public void delete(AggContractAptmentVO[] bills) {

		DeleteBPTemplate<AggContractAptmentVO> bp = new DeleteBPTemplate<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggContractAptmentVO> processer) {
		// TODO ǰ����
		IRule<AggContractAptmentVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggContractAptmentVO> processer) {
		// TODO �����

	}
}
