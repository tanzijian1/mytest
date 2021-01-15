package nc.bs.tg.fischeme.ace.bp;

import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;

/**
 * 修改保存的BP
 * 
 */
public class AceFischemeUpdateBP {

	public AggFIScemeHVO[] update(AggFIScemeHVO[] bills,
			AggFIScemeHVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggFIScemeHVO> bp = new UpdateBPTemplate<AggFIScemeHVO>(
				FischemePluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		//return bp.update(bills, originBills);
		//修改自动添加版本
		bills =bp.update(bills, originBills);
/*		VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO> verBp = new VersionInsertBP<AggFIScemeHVO, AggFISchemeversionHVO>();
		AggFIScemeHVO[] aggVerVO = verBp.insert(bills, null);*/
		return bills;
	}

	private void addAfterRule(CompareAroundProcesser<AggFIScemeHVO> processer) {
		// TODO 后规则
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
		.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
		.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggFIScemeHVO> processer) {
		// TODO 前规则
		IRule<AggFIScemeHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggFIScemeHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setCbilltype("RZ05");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
		.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}

}
