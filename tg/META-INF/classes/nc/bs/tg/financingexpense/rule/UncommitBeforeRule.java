package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class UncommitBeforeRule implements IRule<AggFinancexpenseVO> {

	@Override
	public void process(AggFinancexpenseVO[] vos) {
		// TODO �Զ����ɵķ������
		for (AggFinancexpenseVO aggFinancexpenseVO : vos) {
			aggFinancexpenseVO.getParentVO().setAttributeValue("def19", null);
			aggFinancexpenseVO.getParentVO().setAttributeValue("def20", null);
		}
	}

}
