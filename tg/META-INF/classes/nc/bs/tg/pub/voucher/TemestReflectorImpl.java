package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.temporaryestimate.AggTemest;

public class TemestReflectorImpl extends TGReflector<AggTemest> {

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_temest";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN03);
	}

	@Override
	public Class getBillClass() {
		return AggTemest.class;
	}
}
