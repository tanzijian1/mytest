package nc.bs.tg.renamechangebill.ace.bp;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

/**
 * �޸ı����BP
 * 
 */
public class AceRenameChangeBillUpdateBP {

	public AggRenameChangeBillHVO[] update(AggRenameChangeBillHVO[] bills,
			AggRenameChangeBillHVO[] originBills) {
		// �����޸�ģ��
		UpdateBPTemplate<AggRenameChangeBillHVO> bp = new UpdateBPTemplate<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.UPDATE);
		// ִ��ǰ����
		this.addBeforeRule(bp.getAroundProcesser());
		// ִ�к����
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO �����
		IRule<AggRenameChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("FN18");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggRenameChangeBillHVO> processer) {
		// TODO ǰ����
		IRule<AggRenameChangeBillHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggRenameChangeBillHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("FN18");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
