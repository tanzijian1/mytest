package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;

public class AbTaxTransferReflectorImpl extends TGReflector<AggAbTaxTransferHVO>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_abtaxtransfer_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN21);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggAbTaxTransferHVO.class;
	}

}
