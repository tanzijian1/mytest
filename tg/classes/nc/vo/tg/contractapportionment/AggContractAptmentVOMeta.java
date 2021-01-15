package nc.vo.tg.contractapportionment;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggContractAptmentVOMeta extends AbstractBillMeta{
	
	public AggContractAptmentVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.contractapportionment.ContractAptmentVO.class);
		this.addChildren(nc.vo.tg.contractapportionment.ContractAptmentBVO.class);
	}
}