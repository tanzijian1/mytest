package nc.vo.tg.singleissue_v;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.singleissue_v.SingleIssueVersionVO")

public class AggSingleIssueVersionVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSingleIssueVersionVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SingleIssueVersionVO getParentVO(){
	  	return (SingleIssueVersionVO)this.getParent();
	  }
	  
}