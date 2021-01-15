package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.invoicing.AggInvoicingHead;

public class InvoicingReflectorImpl extends TGReflector<AggInvoicingHead> {

	@Override
	public String getPKFieldName() {
		return "pk_invoicing";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN23);
	}

	@Override
	public Class<AggInvoicingHead> getBillClass() {
		return AggInvoicingHead.class;
	}

}
