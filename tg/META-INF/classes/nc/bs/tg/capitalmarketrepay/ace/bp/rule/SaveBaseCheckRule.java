package nc.bs.tg.capitalmarketrepay.ace.bp.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.GroupCreditBVO;

public class SaveBaseCheckRule implements IRule<AggMarketRepalayVO>{
	@Override
	public void process(AggMarketRepalayVO[] vos) {
		for (AggMarketRepalayVO aggvo : vos) {
			GroupCreditBVO[] bvos = (GroupCreditBVO[])aggvo.getChildren(GroupCreditBVO.class);
			if(bvos != null){
				for (GroupCreditBVO bvo : bvos) {
					if("Y".equals(bvo.getDef3()) && bvo.getDef4()==null){
						ExceptionUtils.wrappBusinessException("【集团授信】选择释放授信时，必须填写释放授信金额");
					}
				}
			}
		}
		
	}

}
