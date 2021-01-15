package nc.vo.tg.approvalpro_v;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggApprovalProVersionVOMeta extends AbstractBillMeta{
	
	public AggApprovalProVersionVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.approvalpro_v.ApprovalProVersionVO.class);
		this.addChildren(nc.vo.tg.approvalpro_v.ProgressCtrVersionVO.class);
		this.addChildren(nc.vo.tg.approvalpro_v.IssueScaleVersionVO.class);
	}
}