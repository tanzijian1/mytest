package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

public class ExhousetransferReflectorImpl extends TGReflector<AggExhousetransferbillHVO>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_exhoutranbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().contains(
				TGCont.CONST_BILLTYPE_FN19);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggExhousetransferbillHVO.class;
	}

}
