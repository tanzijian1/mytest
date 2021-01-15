package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.changebill.AggChangeBillHVO;

public class ChangebillReflectorImpl extends TGReflector<AggChangeBillHVO> {

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_changebill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		return relationInfoVO.getPk_billtype().startsWith(
				TGCont.CONST_BILLTYPE_FN11);

	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggChangeBillHVO.class;
	}

}
