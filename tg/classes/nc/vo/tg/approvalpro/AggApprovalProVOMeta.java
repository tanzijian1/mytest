package nc.vo.tg.approvalpro;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggApprovalProVOMeta extends AbstractBillMeta{
	
	public AggApprovalProVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.approvalpro.ApprovalProVO.class);
		this.addChildren(nc.vo.tg.approvalpro.ProgressCtrVO.class);
		this.addChildren(nc.vo.tg.approvalpro.IssueScaleVO.class);
	}
}