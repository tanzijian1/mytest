package nc.vo.tg.singleissue;

import java.util.HashMap;
import java.util.Map;

import nc.itf.tmpub.version.IVersionAggVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.tg.singleissue_v.ABSRepayVersionVO;
import nc.vo.tg.singleissue_v.AggSingleIssueVersionVO;
import nc.vo.tg.singleissue_v.BondResaleVersionVO;
import nc.vo.tg.singleissue_v.BondTransSaleVersionVO;
import nc.vo.tg.singleissue_v.ConstateExeVersionVO;
import nc.vo.tg.singleissue_v.ContractStateVersionVO;
import nc.vo.tg.singleissue_v.CycleBuyingVersionVO;
import nc.vo.tg.singleissue_v.GroupCreditVersionVO;
import nc.vo.tg.singleissue_v.IssueDetailVersionVO;
import nc.vo.tg.singleissue_v.RepaymentPlanVersionVO;
import nc.vo.tg.singleissue_v.SingleIssueBVersionVO;
import nc.vo.trade.pub.IExAggVO;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.singleissue.SingleIssueVO")

public class AggSingleIssueVO extends AbstractBill implements IExAggVO,IVersionAggVO{
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSingleIssueVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SingleIssueVO getParentVO(){
	  	return (SingleIssueVO)this.getParent();
	  }
	  
	  @Override
		public Class getAggVersionVO() {
			// TODO 自动生成的方法存根
			return AggSingleIssueVersionVO.class;
		}

		@Override
		public Map<Class, Class> getChildVersionVOs() {
			// TODO 自动生成的方法存根
			Map<Class, Class> map = new HashMap<Class,Class>();
			map.put(BondResaleVO.class, BondResaleVersionVO.class);
			map.put(BondTransSaleVO.class, BondTransSaleVersionVO.class);
			map.put(ConstateExeVO.class, ConstateExeVersionVO.class);
			map.put(ContractStateVO.class, ContractStateVersionVO.class);
			map.put(GroupCreditVO.class, GroupCreditVersionVO.class);
			map.put(IssueDetailVO.class, IssueDetailVersionVO.class);
			map.put(RepaymentPlanVO.class, RepaymentPlanVersionVO.class);
			map.put(ABSRepayVO.class, ABSRepayVersionVO.class);
			map.put(CycleBuyingVO.class, CycleBuyingVersionVO.class);
			map.put(SingleIssueBVO.class, SingleIssueBVersionVO.class);
			return map;
		}
}