package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class CheckIsBackToBPM implements IRule<AggFinancexpenseVO>{

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		for (AggFinancexpenseVO aggvo : vos) {
//			if("Y".equals(aggvo.getParentVO().getAttributeValue("def33"))){
//				ExceptionUtils
//				.wrappBusinessException("������֪ͨbpm�˻أ����ܽ������");
//			}
			aggvo.getParentVO().setAttributeValue("def33", "N");
		}
		
	}

}
