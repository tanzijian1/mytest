package nc.vo.tg.masterdata;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.masterdata.MasterDataVO")

public class AggMasterDataVO extends AbstractBill {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = -1160495648661550937L;

	@Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMasterDataVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MasterDataVO getParentVO(){
	  	return (MasterDataVO)this.getParent();
	  }
	  
}