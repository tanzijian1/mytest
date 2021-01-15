package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

public class ExhousetransferReflectorImpl extends TGReflector<AggExhousetransferbillHVO>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_exhoutranbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().contains(
				TGCont.CONST_BILLTYPE_FN19);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggExhousetransferbillHVO.class;
	}

}
