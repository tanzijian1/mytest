package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.outbill.AggOutbillHVO;

public class OutbillReflectorImpl extends TGReflector<AggOutbillHVO>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_outbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN04);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggOutbillHVO.class;
	}

}
