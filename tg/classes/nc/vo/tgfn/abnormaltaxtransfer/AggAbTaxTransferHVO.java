package nc.vo.tgfn.abnormaltaxtransfer;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.abnormaltaxtransfer.AbTaxTransferHVO")

public class AggAbTaxTransferHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggAbTaxTransferHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public AbTaxTransferHVO getParentVO(){
	  	return (AbTaxTransferHVO)this.getParent();
	  }
	  
}