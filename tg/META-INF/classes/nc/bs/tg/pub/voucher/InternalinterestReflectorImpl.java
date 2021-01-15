package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

public class InternalinterestReflectorImpl extends TGReflector<AggInternalinterest>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_internal";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN06);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggInternalinterest.class;
	}

}
