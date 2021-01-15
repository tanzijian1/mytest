package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tg.tartingbill.AggTartingBillVO;

public class TartingBillReflectorImpl extends TGReflector<AggTartingBillVO>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_tartingbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN16);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggTartingBillVO.class;
	}

}
