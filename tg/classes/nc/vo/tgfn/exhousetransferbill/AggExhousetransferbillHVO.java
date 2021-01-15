package nc.vo.tgfn.exhousetransferbill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.exhousetransferbill.ExhousetransferbillHVO")

public class AggExhousetransferbillHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggExhousetransferbillHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ExhousetransferbillHVO getParentVO(){
	  	return (ExhousetransferbillHVO)this.getParent();
	  }
	  
}