package nc.vo.tg.projectbatchlog;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.projectbatchlog.ProjectBatchLogVO")

public class AggProjectBatchLogVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggProjectBatchLogVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ProjectBatchLogVO getParentVO(){
	  	return (ProjectBatchLogVO)this.getParent();
	  }
	  
}