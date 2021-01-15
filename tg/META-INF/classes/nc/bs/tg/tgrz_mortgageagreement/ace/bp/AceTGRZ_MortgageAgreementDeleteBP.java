package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * ��׼����ɾ��BP
 */
public class AceTGRZ_MortgageAgreementDeleteBP {

	public void delete(AggMortgageAgreementVO[] bills) {

		DeleteBPTemplate<AggMortgageAgreementVO> bp = new DeleteBPTemplate<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.DELETE);
		// ����ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ����ִ�к�ҵ�����
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO ǰ����
		IRule<AggMortgageAgreementVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * ɾ����ҵ�����
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO �����

	}
}
