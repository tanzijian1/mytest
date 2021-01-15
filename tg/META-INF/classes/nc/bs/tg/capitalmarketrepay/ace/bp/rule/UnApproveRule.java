package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO;

public class UnApproveRule implements IRule<AggMarketRepalayVO>{

	@Override
	public void process(AggMarketRepalayVO[] vos) {
		// TODO 自动生成的方法存根
		for (AggMarketRepalayVO aggvo : vos) {
			String sql = "select count(1) count from sdfn_marketrepalay sd inner join cmp_paybill cm"
					+ " on sd.pk_marketreplay=cm.pk_upbill and nvl(cm.dr,0)=0"
					+ " where nvl(sd.dr,0)=0 and sd.pk_marketreplay = '"+aggvo.getParentVO().getPk_marketreplay()+"'";
			String asql = "select  count(1) count from sdfn_bondtranssale where def1='"+aggvo.getParentVO().getBillno()+"'"
					+ "	and nvl(dr,0) = 0";
			BaseDAO baseDAO = new BaseDAO();
			try {
				Integer str = (Integer)baseDAO.executeQuery(sql, new ColumnProcessor());
				if(str!=null && str>0){
					ExceptionUtils.wrappBusinessException("已生成付款结算单，不能取消审核。");
				}
				Integer exestr = (Integer)baseDAO.executeQuery(asql, new ColumnProcessor());
				if(exestr!=null && exestr>0){
					ExceptionUtils.wrappBusinessException("已回写到单期发行情况的合同执行情况页签，不能取消审核。");
				}
			} catch (DAOException e) {
				// TODO 自动生成的 catch 块
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}
			MarketRepaleyBVO[] bvos = (MarketRepaleyBVO[])aggvo.getChildren(MarketRepaleyBVO.class);
			for (MarketRepaleyBVO bvo : bvos) {
				if(bvo.getDef1()!=null && "Y".equals(bvo.getDef3())){
					ExceptionUtils.wrappBusinessException("已回写集团授信，不能取消审核。");
				}
			}
			
		}
		
	}

}
