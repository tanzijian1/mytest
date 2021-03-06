package nc.bs.tg.abnormaltaxtransfer.ace.bp;

import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

/**
 * 修改保存的BP
 * 
 */
public class AceAbnormalTaxTransferUpdateBP {

	public AggAbTaxTransferHVO[] update(AggAbTaxTransferHVO[] bills,
			AggAbTaxTransferHVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggAbTaxTransferHVO> bp = new UpdateBPTemplate<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO 后规则
		IRule<AggAbTaxTransferHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("FN21");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggAbTaxTransferHVO> processer) {
		// TODO 前规则
		IRule<AggAbTaxTransferHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggAbTaxTransferHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("FN21");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
