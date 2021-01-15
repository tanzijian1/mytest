package nc.vo.tg.fischemeversion;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.fischemeversion.FISchemeversionHVO")

public class AggFISchemeversionHVO extends AbstractBill {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3528283163938066806L;

	@Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFISchemeversionHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FISchemeversionHVO getParentVO(){
	  	return (FISchemeversionHVO)this.getParent();
	  }
	  
}