package nc.bs.tg.approvalpro.ace.bp;

import nc.bs.pubapp.pub.rule.BillCodeCheckRule;
import nc.bs.pubapp.pub.rule.UpdateBillCodeRule;
import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.vo.tg.approvalpro_v.AggApprovalProVersionVO;


public class AceApprovalproChangeBP {
	public AggApprovalProVO[] change(AggApprovalProVO[] clientFullVOs,
			AggApprovalProVO[] originBills) {
		UpdateBPTemplate<AggApprovalProVO> bp = new UpdateBPTemplate<AggApprovalProVO>(ApprovalproPluginPoint.UPDATE);
		//插入版本信息，并把版本信息更新到新的单据中
		String pk_appro = clientFullVOs[0].getPrimaryKey();
		AggApprovalProVO[] historyVO = getHistory(pk_appro);
		VersionInsertBP<AggApprovalProVO, AggApprovalProVersionVO> verBp = new VersionInsertBP<AggApprovalProVO, AggApprovalProVersionVO>();
		AggApprovalProVO[] insertVO = verBp.insert(historyVO, null);
		clientFullVOs[0].getParentVO().setPk_approversion(insertVO[0].getParentVO().getPk_approversion());
		clientFullVOs[0].getParentVO().setBillversiondate(insertVO[0].getParentVO().getBillversiondate());
		clientFullVOs[0].getParentVO().setApproversionnum(insertVO[0].getParentVO().getApproversionnum());
		
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		
		clientFullVOs =bp.update(clientFullVOs, originBills);
		return clientFullVOs;
	}

	private void addAfterRule(
			CompareAroundProcesser<AggApprovalProVO> processer) {
		// TODO 自动生成的方法存根
		IRule<AggApprovalProVO> rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((BillCodeCheckRule) rule).setCbilltype("SD03");
		((BillCodeCheckRule) rule).setCodeItem("billno");
		((BillCodeCheckRule) rule).setGroupItem("pk_group");
		((BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);
	}

	private void addBeforeRule(
			CompareAroundProcesser<AggApprovalProVO> processer) {
		IRule<AggApprovalProVO> rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggApprovalProVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((UpdateBillCodeRule) ruleCom).setCbilltype("SD03");
		((UpdateBillCodeRule) ruleCom).setCodeItem("billno");
		((UpdateBillCodeRule) ruleCom).setGroupItem("pk_group");
		((UpdateBillCodeRule) ruleCom).setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}
	
	private AggApprovalProVO[] getHistory(String pk_appro) {
		// TODO 自动生成的方法存根
		BillQuery<AggApprovalProVO> billquery = new BillQuery(AggApprovalProVO.class);
		AggApprovalProVO[] aggvo=billquery.query(new String[] {pk_appro});
		return aggvo;
	}
}
