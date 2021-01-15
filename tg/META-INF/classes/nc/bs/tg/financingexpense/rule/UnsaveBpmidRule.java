package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

public class UnsaveBpmidRule implements IRule<AggMortgageAgreementVO>{

	@Override
	public void process(AggMortgageAgreementVO[] vos) {
		// TODO 自动生成的方法存根
		for(AggMortgageAgreementVO vo:vos){
			if(!("Y".equals(vo.getParentVO().getAttributeValue("def10")))){
				vo.getParentVO().setAttributeValue("def20", null);
				vo.getParentVO().setAttributeValue("def19", null);
			}
			vo.getParentVO().setAttributeValue("def10", null);
		}
	}

}
