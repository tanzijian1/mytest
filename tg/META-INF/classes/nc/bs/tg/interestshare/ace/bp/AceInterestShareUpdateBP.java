package nc.bs.tg.interestshare.ace.bp;

import nc.bs.tg.interestshare.ace.rule.FN24_DelBodyAfterRule;
import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tgfn.interestshare.AggIntshareHead;

/**
 * �޸ı����BP
 * 
 */
public class AceInterestShareUpdateBP {

	public AggIntshareHead[] update(AggIntshareHead[] bills,
			AggIntshareHead[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggIntshareHead> bp = new UpdateBPTemplate<AggIntshareHead>(
				InterestSharePluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggIntshareHead> processer) {
		// TODO �����
		IRule<AggIntshareHead> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("FN24");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);
		IRule<AggIntshareHead> delrule =new FN24_DelBodyAfterRule();
		processer.addAfterRule(delrule);
	}

	private void addBeforeRule(CompareAroundProcesser<AggIntshareHead> processer) {
		// TODO ǰ����
		IRule<AggIntshareHead> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggIntshareHead> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("FN24");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
