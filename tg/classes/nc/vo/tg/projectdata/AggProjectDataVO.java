package nc.vo.tg.projectdata;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.projectdata.ProjectDataVO")

public class AggProjectDataVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggProjectDataVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ProjectDataVO getParentVO(){
	  	return (ProjectDataVO)this.getParent();
	  }
	  
}