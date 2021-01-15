package nc.bs.tg.pub.voucher;

import nc.vo.fip.service.FipRelationInfoVO;
import nc.vo.tg.pub.TGCont;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;

public class CostAccrueBillReflectorImpl extends TGReflector<AggCostAccrueBill>{

	@Override
	public String getPKFieldName() {
		// TODO �Զ����ɵķ������
		return "pk_costaccruebill_h";
	}

	@Override
	public boolean needRelect(FipRelationInfoVO relationInfoVO) {
		// TODO �Զ����ɵķ������
		return relationInfoVO.getPk_billtype().equals(
				TGCont.CONST_BILLTYPE_FN08);
	}

	@Override
	public Class getBillClass() {
		// TODO �Զ����ɵķ������
		return AggCostAccrueBill.class;
	}

}