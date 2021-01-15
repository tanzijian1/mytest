package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceTGRZ_MortgageAgreementDeleteBP {

	public void delete(AggMortgageAgreementVO[] bills) {

		DeleteBPTemplate<AggMortgageAgreementVO> bp = new DeleteBPTemplate<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO 前规则
		IRule<AggMortgageAgreementVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO 后规则

	}
}
