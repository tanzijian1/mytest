package nc.vo.tgfn.transferbill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.transferbill.TransferBillHVO")

public class AggTransferBillHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggTransferBillHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public TransferBillHVO getParentVO(){
	  	return (TransferBillHVO)this.getParent();
	  }
	  
}