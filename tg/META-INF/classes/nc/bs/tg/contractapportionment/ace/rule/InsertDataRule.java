package nc.bs.tg.contractapportionment.ace.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;

public class InsertDataRule implements IRule<AggContractAptmentVO> {

	@Override
	public void process(AggContractAptmentVO[] paramArrayOfE) {
		// TODO 自动生成的方法存根
		for(AggContractAptmentVO aggvo : paramArrayOfE){
			aggvo.getParentVO().setBilltype("FN02");
			aggvo.getParentVO().setDbilldate(new UFDate());
		}
	}

}
