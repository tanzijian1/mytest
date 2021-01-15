package nc.bs.tg.standard.ace.bp;

import nc.bs.tg.standard.plugin.bpplugin.StandardPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.tg.standard.AggStandardVO;

/**
 * 标准单据新增BP
 */
public class AceStandardInsertBP {

	public AggStandardVO[] insert(AggStandardVO[] bills) {

		InsertBPTemplate<AggStandardVO> bp = new InsertBPTemplate<AggStandardVO>(
				StandardPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggStandardVO> processor) {
		// TODO 新增后规则
		IRule<AggStandardVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("RZ01");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCodeItem("code");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processor.addAfterRule(rule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggStandardVO> processer) {
		// TODO 新增前规则
		IRule<AggStandardVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("RZ01");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCodeItem("code");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);

	}
}
