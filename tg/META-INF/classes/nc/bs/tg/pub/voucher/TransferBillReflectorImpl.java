package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;

public class TransferBillReflectorImpl extends TGReflector<AggTransferBillHVO>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_transferbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN15);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggTransferBillHVO.class;
	}

}
