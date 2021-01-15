package nc.vo.tg.singleissue;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSingleIssueVOMeta extends AbstractBillMeta{
	
	public AggSingleIssueVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.singleissue.SingleIssueVO.class);
		this.addChildren(nc.vo.tg.singleissue.ContractStateVO.class);
		this.addChildren(nc.vo.tg.singleissue.BondResaleVO.class);
		this.addChildren(nc.vo.tg.singleissue.RepaymentPlanVO.class);
		this.addChildren(nc.vo.tg.singleissue.BondTransSaleVO.class);
		this.addChildren(nc.vo.tg.singleissue.ConstateExeVO.class);
		this.addChildren(nc.vo.tg.singleissue.GroupCreditVO.class);
		this.addChildren(nc.vo.tg.singleissue.IssueDetailVO.class);
		this.addChildren(nc.vo.tg.singleissue.SingleIssueBVO.class);
		this.addChildren(nc.vo.tg.singleissue.CycleBuyingVO.class);
		this.addChildren(nc.vo.tg.singleissue.ABSRepayVO.class);
	}
}