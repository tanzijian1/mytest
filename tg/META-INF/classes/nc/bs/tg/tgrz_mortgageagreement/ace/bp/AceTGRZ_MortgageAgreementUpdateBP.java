package nc.bs.tg.tgrz_mortgageagreement.ace.bp;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

/**
 * �޸ı����BP
 * 
 */
public class AceTGRZ_MortgageAgreementUpdateBP {

	public AggMortgageAgreementVO[] update(AggMortgageAgreementVO[] bills,
			AggMortgageAgreementVO[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggMortgageAgreementVO> bp = new UpdateBPTemplate<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO �����
		IRule<AggMortgageAgreementVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("RZ04");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggMortgageAgreementVO> processer) {
		// TODO ǰ����
		IRule<AggMortgageAgreementVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggMortgageAgreementVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("RZ04");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
