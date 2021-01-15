package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.pub.TGCont;

public class InvoiceBillReflectorImpl extends TGReflector<AggInvoiceBillVO>{

	@Override
	public String getPKFieldName() {
		// TODO 自动生成的方法存根
		return "pk_invoicebill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO 自动生成的方法存根
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN05);
	}

	@Override
	public Class getBillClass() {
		// TODO 自动生成的方法存根
		return AggInvoiceBillVO.class;
	}

}
