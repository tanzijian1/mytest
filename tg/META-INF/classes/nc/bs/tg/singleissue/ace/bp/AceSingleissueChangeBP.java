package nc.bs.tg.singleissue.ace.bp;

import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.bs.tmpub.version.VersionInsertBP;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.vo.tg.singleissue_v.AggSingleIssueVersionVO;

public class AceSingleissueChangeBP {

	public AggSingleIssueVO[] change(AggSingleIssueVO[] clientFullVOs,
			AggSingleIssueVO[] originBills) {
		UpdateBPTemplate<AggSingleIssueVO> bp = new UpdateBPTemplate<AggSingleIssueVO>(SingleissuePluginPoint.UPDATE);
		String pk_singleissue = clientFullVOs[0].getPrimaryKey();
		AggSingleIssueVO[] historyVO = this.getHistory(pk_singleissue);
		VersionInsertBP<AggSingleIssueVO, AggSingleIssueVersionVO> verBp = new VersionInsertBP<AggSingleIssueVO,AggSingleIssueVersionVO>();
		AggSingleIssueVO[] insertVO = verBp.insert(historyVO, null);
		clientFullVOs[0].getParentVO().setPk_singleversion(insertVO[0].getParentVO().getPk_singleversion());
		clientFullVOs[0].getParentVO().setBillversiondate(insertVO[0].getParentVO().getBillversiondate());
		clientFullVOs[0].getParentVO().setApproversionnum(insertVO[0].getParentVO().getApproversionnum());

		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		
		clientFullVOs =bp.update(clientFullVOs, originBills);
		return clientFullVOs;
	}
	
	private void addAfterRule(CompareAroundProcesser<AggSingleIssueVO> processer) {
		// TODO 后规则
		IRule<AggSingleIssueVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("SD06");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggSingleIssueVO> processer) {
		// TODO 前规则
		IRule<AggSingleIssueVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggSingleIssueVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("SD06");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
	}
	
	private AggSingleIssueVO[] getHistory(String pk_singleissue) {
		// TODO 自动生成的方法存根
		BillQuery<AggSingleIssueVO> billquery = new BillQuery(AggSingleIssueVO.class);
		AggSingleIssueVO[] aggvo=billquery.query(new String[] {pk_singleissue});
		return aggvo;
	}
}
