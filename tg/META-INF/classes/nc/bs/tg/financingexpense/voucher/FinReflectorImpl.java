package nc.bs.tg.financingexpense.voucher;
import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

public class FinReflectorImpl extends TGFINReflToll<AggFinancexpenseVO>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_finexpense";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				"RZ06-Cxx-002");
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggFinancexpenseVO.class;
	}

}
