package nc.bs.tg.pub.voucher;


import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.pub.TGCont;

public class ContractAptmentReflectorImpl extends TGReflector<AggContractAptmentVO>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_contractaptment_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN02);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggContractAptmentVO.class;
	}

}
