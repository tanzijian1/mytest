package nc.vo.tgfn.renamechangebill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.renamechangebill.RenameChangeBillHVO")

public class AggRenameChangeBillHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggRenameChangeBillHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public RenameChangeBillHVO getParentVO(){
	  	return (RenameChangeBillHVO)this.getParent();
	  }
	  
}