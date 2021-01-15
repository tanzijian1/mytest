package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.distribution.AggDistribution;

public class DistributionReflectorImpl extends TGReflector<AggDistribution>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_distribution";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().contains(
				TGCont.CONST_BILLTYPE_FN13);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggDistribution.class;
	}

}
