package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.distribution.AggDistribution;

public class DistributionReflectorImpl extends TGReflector<AggDistribution>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_distribution";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().contains(
				TGCont.CONST_BILLTYPE_FN13);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggDistribution.class;
	}

}
