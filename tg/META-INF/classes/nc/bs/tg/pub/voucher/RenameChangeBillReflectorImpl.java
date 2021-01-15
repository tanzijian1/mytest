package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

public class RenameChangeBillReflectorImpl extends TGReflector<AggRenameChangeBillHVO>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_renamechbill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN18);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggRenameChangeBillHVO.class;
	}

}
