package nc.vo.tg.fischeme;

import java.util.HashMap;
import java.util.Map;

import nc.itf.tmpub.version.IVersionAggVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.tg.fischemeversion.AggFISchemeversionHVO;
import nc.vo.tg.fischemeversion.CapmarketversionBVO;
import nc.vo.tg.fischemeversion.FISchemeversionBVO;
import nc.vo.tg.fischemeversion.NFISchemeversionBVO;
import nc.vo.trade.pub.IExAggVO;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.fischeme.FIScemeHVO")

public class AggFIScemeHVO extends AbstractBill implements  IExAggVO, IVersionAggVO {
	
	 

	/**
	 * 
	 */
	private static final long serialVersionUID = 7783178632247683668L;

	@Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFIScemeHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FIScemeHVO getParentVO(){
	  	return (FIScemeHVO)this.getParent();
	  }
	  @Override
		public Class getAggVersionVO() {
			
			return AggFISchemeversionHVO.class;
		}

		@Override
		public Map<Class, Class> getChildVersionVOs() {
			Map<Class, Class> map = new HashMap<Class, Class>();
			map.put(FISchemeBVO.class, FISchemeversionBVO.class);
			map.put(NFISchemeBVO.class, NFISchemeversionBVO.class);
			map.put(CapmarketBVO.class, CapmarketversionBVO.class);
			return map;
		}
}