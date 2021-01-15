package nc.vo.tg.approvalpro_v;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.approvalpro_v.ApprovalProVersionVO")

public class AggApprovalProVersionVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggApprovalProVersionVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ApprovalProVersionVO getParentVO(){
	  	return (ApprovalProVersionVO)this.getParent();
	  }
	  
}