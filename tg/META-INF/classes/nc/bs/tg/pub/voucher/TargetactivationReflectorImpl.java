package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.targetactivation.AggTargetactivation;

public class TargetactivationReflectorImpl extends TGReflector<AggTargetactivation>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_target";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN17);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggTargetactivation.class;
	}

}
