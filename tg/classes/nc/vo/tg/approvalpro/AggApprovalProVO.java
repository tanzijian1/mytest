package nc.vo.tg.approvalpro;

import java.util.HashMap;
import java.util.Map;

import nc.itf.tmpub.version.IVersionAggVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.tg.approvalpro_v.AggApprovalProVersionVO;
import nc.vo.tg.approvalpro_v.IssueScaleVersionVO;
import nc.vo.tg.approvalpro_v.ProgressCtrVersionVO;
import nc.vo.trade.pub.IExAggVO;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.approvalpro.ApprovalProVO")

public class AggApprovalProVO extends AbstractBill implements IExAggVO,IVersionAggVO{
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggApprovalProVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ApprovalProVO getParentVO(){
	  	return (ApprovalProVO)this.getParent();
	  }

	@Override
	public Class getAggVersionVO() {
		// TODO 自动生成的方法存根
		return AggApprovalProVersionVO.class;
	}

	@Override
	public Map<Class, Class> getChildVersionVOs() {
		// TODO 自动生成的方法存根
		Map<Class, Class> map = new HashMap<Class,Class>();
		map.put(IssueScaleVO.class, IssueScaleVersionVO.class);
		map.put(ProgressCtrVO.class, ProgressCtrVersionVO.class);
		return map;
	}
	  
	  
	  
}