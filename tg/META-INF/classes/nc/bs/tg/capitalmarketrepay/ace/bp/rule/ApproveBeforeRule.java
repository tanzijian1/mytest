package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import java.util.List;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;

public class ApproveBeforeRule implements IRule<AggMarketRepalayVO>{
	IUAPQueryBS query = null;

	public IUAPQueryBS getQuery() {
		if (query == null) {
			query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return query;
	}
	@Override
	public void process(AggMarketRepalayVO[] vos) {
		// TODO 自动生成的方法存根
		MarketRepaleyBVO[] mvos = (MarketRepaleyBVO[])vos[0].getChildren(MarketRepaleyBVO.class);
		for (MarketRepaleyBVO mvo : mvos) {
			String sql = "select a1.def4,a1.def5 from sdfn_marketrepaley_b a1 left join sdfn_marketrepalay a2 "
					+ " on a1.pk_marketreplay = a2.pk_marketreplay  where a1.def1='"+mvo.getDef1()+"' "
					+ " and nvl(a1.dr,0)=0 and a2.approvestatus=1";
			String sqlsingle = "select def3,def4,def1 from sdfn_repaymentplan where pk_repayplan='"+mvo.getDef1()+"' and nvl(dr,0)=0";
			try {
				Map<String, String> mapsingle = (Map<String, String>)getQuery().executeQuery(sqlsingle.toString(), new MapProcessor());
				//本金
				UFDouble am = mapsingle.get("def3")==null?UFDouble.ZERO_DBL:new UFDouble(mapsingle.get("def3"));
				//利息
				UFDouble in = mapsingle.get("def4")==null?UFDouble.ZERO_DBL:new UFDouble(mapsingle.get("def4"));
				
				UFDouble amount = UFDouble.ZERO_DBL;//还本
				UFDouble interest = UFDouble.ZERO_DBL;//还息
				List<Map<String, String>> mapList = (List<Map<String, String>>) getQuery().executeQuery(sql.toString(), new MapListProcessor());
				for (Map<String, String> map : mapList) {
					amount = amount.add(map.get("def4")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def4")));
					interest = interest.add(map.get("def5")==null?UFDouble.ZERO_DBL:new UFDouble(map.get("def5")));
				}
				if((mvo.getDef4() == null || Double.valueOf(mvo.getDef4()).doubleValue()==0)
					&& (mvo.getDef5() == null || Double.valueOf(mvo.getDef5()).doubleValue()==0)){
					ExceptionUtils.wrappBusinessException("编号【"+mapsingle.get("def1")+"】的本息为0");
				}
				amount = amount.add(mvo.getDef4()==null?UFDouble.ZERO_DBL:new UFDouble(mvo.getDef4()));
				interest = interest.add(mvo.getDef5()==null?UFDouble.ZERO_DBL:new UFDouble(mvo.getDef5()));
				if(amount.compareTo(am)>0){
					ExceptionUtils.wrappBusinessException("编号【"+mapsingle.get("def1")+"】的总还本金额大于当前还款计划的还本金额");
				}
				if(interest.compareTo(in)>0){
					ExceptionUtils.wrappBusinessException("编号【"+mapsingle.get("def1")+"】的总还息金额大于当前还款计划的还息金额");
				}
			} catch (BusinessException e) {
				e.printStackTrace();
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
		}
	}
}
