package nc.vo.tg.singleissue_v;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSingleIssueVersionVOMeta extends AbstractBillMeta{
	
	public AggSingleIssueVersionVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.singleissue_v.SingleIssueVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.RepaymentPlanVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.BondTransSaleVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.BondResaleVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.ContractStateVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.ConstateExeVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.GroupCreditVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.IssueDetailVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.SingleIssueBVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.CycleBuyingVersionVO.class);
		this.addChildren(nc.vo.tg.singleissue_v.ABSRepayVersionVO.class);
	}
}