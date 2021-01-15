package nc.bs.tg.marginworkorder.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.vo.tgfn.marginworkorder.MarginHVO;
import nc.vo.tgfp.pub.voucher.FPAbstractReflector;

public class MargicWorkReflectorimpl extends FPAbstractReflector<AggMarginHVO> {

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_marhead";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN22);
	}

	@Override
	public Class<AggMarginHVO> getBillClass() {
		// TODO 自动生成的方法存根
		return AggMarginHVO.class;
	}

}
