package nc.bs.tg.costaccruebill.ace.bp;

import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;

/**
 * �޸ı����BP
 * 
 */
public class AceCostAccrueBillUpdateBP {

	public AggCostAccrueBill[] update(AggCostAccrueBill[] bills,
			AggCostAccrueBill[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggCostAccrueBill> bp = new UpdateBPTemplate<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggCostAccrueBill> processer) {
		// TODO �����
		IRule<AggCostAccrueBill> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("FN08");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggCostAccrueBill> processer) {
		// TODO ǰ����
		IRule<AggCostAccrueBill> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggCostAccrueBill> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("FN08");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
